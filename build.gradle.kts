plugins {
    kotlin("jvm") version "1.5.10"
    `java-gradle-plugin`
    `maven-publish`
}

group = "com.javatar"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["kotlin"])
        }
        repositories {
            maven {
                url = uri("http://legionkt.com:8085/repository/rsps-studios-private/")
                credentials {
                    username = project.properties["myNexusUsername"] as String
                    password = project.properties["myNexusPassword"] as String
                }
            }
        }
    }
}