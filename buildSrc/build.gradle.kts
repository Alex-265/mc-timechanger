import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    maven("https://maven.blamejared.com") {
        name = "BlameJared"
    }
}

dependencies {
    gradleApi()
    implementation("com.blamejared:gradle-mod-utils:1.0.5")
    implementation("net.darkhax.curseforgegradle:CurseForgeGradle:1.1.15")
    implementation("com.modrinth.minotaur:Minotaur:2.+")
}