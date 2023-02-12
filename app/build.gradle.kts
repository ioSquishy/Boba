/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/7.3/userguide/building_java_projects.html
 */

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    id("application")
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
    maven(url = "https://jitpack.io");
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:30.1.1-jre")

    implementation("org.javacord:javacord:3.5.0")
    implementation("de.btobastian.sdcf4j:sdcf4j-javacord:v1.0.10")
    implementation("de.btobastian.sdcf4j:sdcf4j-core:v1.0.10")

    implementation("io.github.cdimascio:dotenv-java:2.2.4")
}

application {
    // Define the main class for the application.
    mainClass.set("boba.App")
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}