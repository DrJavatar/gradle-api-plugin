plugins {
    kotlin("jvm") version "1.5.10"
    `java-gradle-plugin`
}

group = "com.javatar"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}
