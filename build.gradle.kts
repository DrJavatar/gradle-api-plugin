plugins {
    kotlin("jvm") version "1.5.21"
    `java-gradle-plugin`
    `maven-publish`
}

group = "com.javatar"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        setUrl("https://plugins.gradle.org/m2/")
    }
}

gradlePlugin {
    plugins {
        create("rsde-api-plugin") {
            id = "com.javatar.rsdeapi"
            implementationClass = "com.javatar.gradle.plugin.RspsStudiosPlugin"
        }
    }
}


dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("gradle-plugin"))
    implementation("org.openjfx:javafx-plugin:0.0.10")
    implementation("org.zeroturnaround:zt-exec:1.12")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    targetCompatibility = "11"
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["kotlin"])
        }
        repositories {
            maven {
                url = uri("http://legionkt.com:8085/repository/maven-snapshots/")
                isAllowInsecureProtocol = true
                credentials {
                    username = project.properties["myNexusUsername"] as String
                    password = project.properties["myNexusPassword"] as String
                }
            }
        }
    }
}