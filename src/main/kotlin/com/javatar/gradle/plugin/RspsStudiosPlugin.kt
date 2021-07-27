package com.javatar.gradle.plugin

import com.javatar.gradle.plugin.configurations.RspsStudiosPluginConfiguration
import com.javatar.gradle.plugin.tasks.AssemblePluginTask
import com.javatar.gradle.plugin.tasks.PluginTask
import com.javatar.gradle.plugin.tasks.RunIDETask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.internal.impldep.org.codehaus.plexus.util.Os
import org.gradle.jvm.tasks.Jar
import org.openjfx.gradle.JavaFXOptions
import java.io.File
import java.nio.file.Path

class RspsStudiosPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target.repositories) {
            maven {
                it.setUrl("http://legionkt.com:8085/repository/maven-public/")
                it.isAllowInsecureProtocol = true
            }
            maven {
                it.setUrl("https://oss.sonatype.org/content/repositories/snapshots/")
            }
        }

        target.pluginManager.apply("org.jetbrains.kotlin.kapt")
        target.pluginManager.apply("org.openjfx.javafxplugin")
        target.pluginManager.apply("java-library")

        target.extensions.configure<JavaFXOptions>("javafx") {
            it.modules(
                "javafx.base",
                "javafx.controls",
                "javafx.graphics",
                "javafx.fxml",
                "javafx.web"
            )
        }

        with(target.dependencies) {
            add(
                "implementation",
                "com.javatar:api:0.1-SNAPSHOT"
            )
            add(
                "compileOnly",
                "no.tornado:tornadofx:2.0.0-SNAPSHOT"
            )
            add(
                "compileOnly",
                "org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.4.2"
            )
            add(
                "compileOnly",
                "org.controlsfx:controlsfx:11.0.3"
            )
            add(
                "compileOnly",
                "de.jensd:fontawesomefx-fontawesome:4.7.0-9.1.2"
            )
            add(
                "compileOnly",
                "com.displee:rs-cache-library:6.8"
            )
            add(
                "compileOnly",
                "io.insert-koin:koin-core:3.1.2"
            )
            add(
                "compileOnly",
                "org.pf4j:pf4j:3.6.0"
            )
            add(
                "kotlinKaptWorkerDependencies",
                "org.pf4j:pf4j:3.6.0"
            )
        }

        val config = target.extensions.create("rsdeplugin", RspsStudiosPluginConfiguration::class.java)

        target.tasks.named("jar", Jar::class.java) {
            it.manifest { m ->
                m.attributes["Plugin-Class"] = config.pluginClass
                m.attributes["Plugin-Id"] = config.pluginId
                m.attributes["Plugin-Version"] = config.pluginVersion
                m.attributes["Plugin-Provider"] = config.pluginProvider
                m.attributes["Plugin-Description"] = config.pluginDescription
            }
        }

        target.configurations.create("plugin") {
            it.isTransitive = false
            target.configurations.named("api").get().extendsFrom(it)
        }

        target.tasks.create("plugin", PluginTask::class.java, target, config)
        target.tasks.create("assemblePlugin", AssemblePluginTask::class.java, target, config)
        target.tasks.create("runIDE", RunIDETask::class.java, target, config)

    }

    private fun Project.testPluginTask(config: RspsStudiosPluginConfiguration) {
        tasks.register("runIDE") {
            val dir = config.rsdeDirectory.get()
            val bin = Path.of(dir, "bin")
            if(Os.isFamily(Os.FAMILY_WINDOWS)) {
                val app = bin.resolve("application.bat").toAbsolutePath().toString()
                Runtime.getRuntime().exec(app)
            } else if(Os.isFamily(Os.FAMILY_MAC) || Os.isFamily(Os.FAMILY_UNIX)) {
                val app = bin.resolve("application")
                Runtime.getRuntime().exec("./$app")
            }
        }
    }

    private fun Project.assemblePluginTask(config: RspsStudiosPluginConfiguration) {


        tasks.register("assemblePlugin", Copy::class.java) { cp ->
            cp.from(tasks.named("plugin"))
            val file = File(config.pluginDirectory.get())
            file.listFiles()?.map { it.deleteRecursively() }
            cp.into(file)
        }

    }

    private fun Project.pluginTask(config: RspsStudiosPluginConfiguration) {

        tasks.register("plugin", Jar::class.java) {
            it.archiveBaseName.set("plugin-${config.pluginId}")
            it.archiveVersion.set(config.pluginVersion)
            it.into("classes") { cs ->
                cs.with(tasks.named("jar", Jar::class.java).get())
            }
            it.dependsOn(configurations.getAt("runtimeClasspath"))
            it.into("lib") { cs ->
                cs.from({
                    configurations.named("plugin").get().filter { s -> s.name.endsWith("jar") }
                })
            }
            it.archiveExtension.set("zip")
        }

    }

}