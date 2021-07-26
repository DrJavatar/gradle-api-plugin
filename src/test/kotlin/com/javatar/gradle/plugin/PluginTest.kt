package com.javatar.gradle.plugin

import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test

class PluginTest {

    @Test
    fun test() {

        val target = ProjectBuilder.builder().build()

        target.pluginManager.apply("com.javatar.rsdeapi")

    }

}