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
