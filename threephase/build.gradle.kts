import configurations.Languages.attachRemoteRepositories
import configurations.Languages.configureJava
import configurations.Publications.configureMavenPublication
import configurations.Publications.configureSignatures
import configurations.Frameworks.configureJUnit5

description = "A copy of Chen Shuang's 4x4 scrambler."

plugins {
    `java-library`
    `maven-publish`
    signing
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

configureJava()
configureMavenPublication("scrambler-threephase")
configureSignatures(publishing)

attachRemoteRepositories()

dependencies {
    implementation(project(":min2phase"))
    implementation(libs.slf4j.api)
}

configureJUnit5()

application {
    mainClass.set("cs.threephase.App")
}

// Create an executable "fat" JAR containing all dependencies
tasks.withType(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class.java) {
    archiveBaseName.set("threephase-solver")
    // No classifier so archive will be like threephase-solver-<version>.jar
    archiveClassifier.set("")
    manifest {
        attributes("Main-Class" to "cs.threephase.App")
    }
}

// Make the regular build produce the fat JAR
tasks.named("build") {
    dependsOn("shadowJar")
}
