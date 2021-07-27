package com.javatar.gradle.plugin.tasks

import com.javatar.gradle.plugin.configurations.RspsStudiosPluginConfiguration
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.jvm.tasks.Jar
import javax.inject.Inject

open class PluginTask @Inject constructor(@Input val target: Project, @Input val config: RspsStudiosPluginConfiguration) : Jar() {

    @TaskAction
    fun action() {
        archiveBaseName.set("plugin-${config.pluginId}")
        archiveVersion.set(config.pluginVersion)
        into("classes") { cs ->
            cs.with(target.tasks.named("jar", Jar::class.java).get())
        }
        dependsOn(target.configurations.getAt("runtimeClasspath"))
        into("lib") { cs ->
            cs.from({
                target.configurations.named("plugin").get().filter { s -> s.name.endsWith("jar") }
            })
        }
        archiveExtension.set("zip")
    }

}