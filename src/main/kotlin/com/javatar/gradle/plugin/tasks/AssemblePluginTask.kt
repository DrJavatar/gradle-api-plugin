package com.javatar.gradle.plugin.tasks

import com.javatar.gradle.plugin.configurations.RspsStudiosPluginConfiguration
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject

open class AssemblePluginTask @Inject constructor(@Input val target: Project, @Input val config: RspsStudiosPluginConfiguration) : Copy() {
    @TaskAction
    fun action() {
        from(target.tasks.named("plugin"))
        val file = File(config.pluginDirectory.get())
        file.listFiles()?.map { it.deleteRecursively() }
        into(file)
    }
}