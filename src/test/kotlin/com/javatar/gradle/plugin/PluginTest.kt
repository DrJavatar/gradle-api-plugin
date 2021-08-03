package com.javatar.gradle.plugin

import com.javatar.gradle.plugin.configurations.RspsStudiosPluginConfiguration
import org.gradle.internal.impldep.org.junit.Ignore
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test

class PluginTest {

    fun test() {

        val target = ProjectBuilder.builder().build()

        target.pluginManager.apply("com.javatar.rsdeapi")

        target.extensions.configure<RspsStudiosPluginConfiguration>("rsdeplugin") {
            it.pluginId.set("test")
            it.pluginVersion.set("1.0")
            it.pluginClass.set("com.javatar.test")
            it.pluginProvider.set("rsps-studios")
            it.pluginDescription.set("A test plugin")
            it.rsdeDirectory.set("test")
        }


    }

}