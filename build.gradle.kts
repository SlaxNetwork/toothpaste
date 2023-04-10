plugins {
    kotlin("jvm") version "1.8.20"
    kotlin("plugin.serialization") version "1.8.20"

    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "io.github.slaxnetwork"
version = "0.0.1"

repositories {
    mavenLocal()
    mavenCentral()

    maven(url = "https://jitpack.io")

}

dependencies {
    implementation("com.github.Minestom.Minestom:Minestom:-SNAPSHOT")

    implementation("net.kyori:adventure-api:4.13.0")
    implementation("net.kyori:adventure-text-minimessage:4.13.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0-Beta")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

    implementation("com.github.shynixn.mccoroutine:mccoroutine-minestom-api:2.11.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-minestom-core:2.11.0")
}

tasks.jar {
    manifest {
        attributes("Main-Class" to "io.github.slaxnetwork.MainKt")
        attributes("Multi-Release" to true)
    }
}