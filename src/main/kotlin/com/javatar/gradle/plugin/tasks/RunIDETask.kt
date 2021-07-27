package com.javatar.gradle.plugin.tasks

import com.javatar.gradle.plugin.configurations.RspsStudiosPluginConfiguration
import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.nio.file.Path
import javax.inject.Inject

open class RunIDETask @Inject constructor(@Input val target: Project, @Input val config: RspsStudiosPluginConfiguration) : DefaultTask() {

    @TaskAction
    fun action() {
        val dir = config.rsdeDirectory.get()
        val bin = Path.of(dir, "bin")
        if(Os.isFamily(Os.FAMILY_WINDOWS)) {
            val app = bin.resolve("application.bat").toAbsolutePath().toString()
            Runtime.getRuntime().exec(app)
        } else if(Os.isFamily(Os.FAMILY_MAC) || Os.isFamily(Os.FAMILY_UNIX)) {
            val app = bin.resolve("application").toAbsolutePath().toString()
            Runtime.getRuntime().exec(app)
        }
    }

}