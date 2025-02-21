import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    kotlin("jvm")
}

group = "com.su"
version = "0.0.1-SNAPSHOT"
archivesName = "contract"
repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation("org.json:json:20250107")
    implementation("com.google.code.gson:gson:2.12.1")
    testImplementation(kotlin("test"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
tasks {
    "shadowJar"(ShadowJar::class) {
        archiveBaseName = "chaincode"
        archiveVersion = null
        archiveClassifier = null
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to "org.hyperledger.fabric.contract.ContractRouter"))
        }
    }
}