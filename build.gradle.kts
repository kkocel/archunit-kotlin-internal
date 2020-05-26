plugins {
    java
    kotlin("jvm") version "1.3.72"
}

group = "tech.kocel.kotlin.archunit.internal"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.tngtech.archunit:archunit:0.13.1")
    testImplementation("junit", "junit", "4.12")
    implementation(kotlin("reflect"))
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks.wrapper {
    gradleVersion = "6.3"
}
