package com.javatar.gradle.plugin

import com.javatar.gradle.plugin.configurations.RspsStudiosPluginConfiguration
import com.javatar.gradle.plugin.tasks.RunIDETask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.tasks.Copy
import org.gradle.jvm.tasks.Jar
import org.gradle.tooling.provider.model.ToolingModelBuilderRegistry
import org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin
import org.jetbrains.kotlin.gradle.model.Kapt
import org.openjfx.gradle.JavaFXOptions
import org.openjfx.gradle.JavaFXPlugin
import java.io.File
import javax.inject.Inject

class RspsStudiosPlugin @Inject internal constructor(private val registry: ToolingModelBuilderRegistry) : Plugin<Project> {

    override fun apply(target: Project) {
        val config = target.extensions.create("rsdeplugin", RspsStudiosPluginConfiguration::class.java)
        with(target.repositories) {
            maven {
                it.setUrl("http://legionkt.com:8085/repository/maven-public/")
                it.isAllowInsecureProtocol = true
            }
            maven {
                it.setUrl("https://oss.sonatype.org/content/repositories/snapshots/")
            }
        }

        target.pluginManager.apply(JavaFXPlugin::class.java)
        target.pluginManager.apply(JavaLibraryPlugin::class.java)
        target.pluginManager.apply(Kapt3GradleSubplugin::class.java)



        target.extensions.configure<JavaFXOptions>("javafx") {
            it.version = "16"
            it.modules(
                "javafx.base",
                "javafx.controls",
                "javafx.graphics",
                "javafx.fxml",
                "javafx.web",
                "javafx.swing"
            )
        }

        with(target.dependencies) {
            add(
                "compileOnly",
                "org.jetbrains.kotlin:kotlin-reflect:1.5.21"
            )
            add(
                "compileOnly",
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
                "kapt",
                "org.pf4j:pf4j:3.6.0"
            )
        }

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

        target.tasks.register("plugin", Jar::class.java) {
            it.dependsOn(
                target.tasks.named("build"),
                target.configurations.getAt("runtimeClasspath")
            )
            it.archiveBaseName.set("plugin-${config.pluginId.get()}")
            it.archiveVersion.set(config.pluginVersion.get())
            it.archiveExtension.set("zip")
            it.into("classes") { cs ->
                cs.with(target.tasks.named("jar", Jar::class.java).get())
            }
            it.into("lib") { cs ->
                cs.from({
                    target.configurations.named("plugin").get().filter { s -> s.name.endsWith("jar") }
                })
            }
        }
        target.tasks.register("assemblePlugin", Copy::class.java) {
            it.dependsOn(target.tasks.named("plugin"))
            it.from(target.tasks.named("plugin"))
            val file = File(config.pluginDirectory.get())
            file.listFiles()?.map { f -> f.deleteRecursively() }
            it.into(file)
        }
        target.tasks.register("runIDE", RunIDETask::class.java) {
            it.dependsOn(target.tasks.named("assemblePlugin"))
            it.config.set(config)
        }
    }
}