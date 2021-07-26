package com.javatar.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

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
                "org.koin:koin-core:2.2.1"
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

        /*
        compileOnly(kotlin("stdlib"))
        compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.4.2")
        compileOnly("no.tornado:tornadofx:2.0.0-SNAPSHOT")
        compileOnly("org.controlsfx:controlsfx:11.0.3")
        compileOnly("de.jensd:fontawesomefx-fontawesome:4.7.0-9.1.2")
        compileOnly("com.displee:rs-cache-library:6.8")
        compileOnly("org.koin:koin-core:2.2.1")
         */

    }
}