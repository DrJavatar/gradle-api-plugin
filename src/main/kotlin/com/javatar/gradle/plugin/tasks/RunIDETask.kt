package com.javatar.gradle.plugin.tasks

import com.javatar.gradle.plugin.configurations.RspsStudiosPluginConfiguration
import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.zeroturnaround.exec.ProcessExecutor
import java.nio.file.Path

abstract class RunIDETask : DefaultTask() {

    @get:Input
    abstract val config: Property<RspsStudiosPluginConfiguration>

    @TaskAction
    fun action() {
        val config = config.get()
        val dir = config.rsdeDirectory.get()
        val bin = Path.of(dir, "bin")
        if(Os.isFamily(Os.FAMILY_WINDOWS)) {
            val app = bin.resolve("application.bat").toAbsolutePath().toString()
            ProcessExecutor().command(app)
                .redirectOutput(System.out)
                .redirectError(System.err)
                .execute()
        } else if(Os.isFamily(Os.FAMILY_MAC) || Os.isFamily(Os.FAMILY_UNIX)) {
            val app = bin.resolve("application").toAbsolutePath().toString()
            ProcessExecutor().command(app)
                .redirectOutput(System.out)
                .redirectError(System.err)
                .execute()
        } else {
            error("Unrecognized Operating System Family")
        }
    }

}