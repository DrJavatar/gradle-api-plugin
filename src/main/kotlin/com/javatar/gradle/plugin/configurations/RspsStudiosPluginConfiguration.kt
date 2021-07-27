package com.javatar.gradle.plugin.configurations

import org.gradle.api.provider.Property
import org.jetbrains.kotlin.konan.file.File

abstract class RspsStudiosPluginConfiguration {
    abstract val pluginId: Property<String>
    abstract val pluginClass: Property<String>
    abstract val pluginProvider: Property<String>
    abstract val pluginDescription: Property<String>
    abstract val pluginVersion: Property<String>

    abstract val pluginDirectory: Property<String>
    abstract val rsdeDirectory: Property<String>

    init {
        pluginId.convention("rsps-studios-plugin")
        pluginClass.convention("null")
        pluginProvider.convention("Rsps Studios")
        pluginDescription.convention("A Rsps Studios Plugin")
        pluginVersion.convention("1.0")
        rsdeDirectory.convention("./")
        pluginDirectory.convention("${System.getProperty("user.home")}${File.separator}rsps-studios${File.separator}plugins")
    }
}