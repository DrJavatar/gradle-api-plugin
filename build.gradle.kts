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
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21")
    runtimeOnly("org.openjfx:javafx-plugin:0.0.10")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    targetCompatibility = "16"
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["kotlin"])
        }
        repositories {
            maven {
                url = uri("http://legionkt.com:8085/repository/rsps-studios-private/")
                isAllowInsecureProtocol = true
                credentials {
                    username = project.properties["myNexusUsername"] as String
                    password = project.properties["myNexusPassword"] as String
                }
            }
        }
    }
}