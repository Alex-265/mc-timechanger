plugins {
    id("dev.kikugie.stonecutter")
    id("me.modmuss50.mod-publish-plugin") version "2.2.0" apply false
}

stonecutter active "26.1.2-fabric"

stonecutter parameters {
    val (version, loader) = current.project.split('-', limit = 2)

    properties {
        tags(version, loader)
    }

    constants {
        match(loader, "fabric", "neoforge")
    }
}

val releaseTargets = listOf("1.21.11", "26.1.2", "26.2", "26.3")
    .flatMap { version -> listOf("$version-fabric", "$version-neoforge") }
val publishDryRun = providers.gradleProperty("publishDryRun").map(String::toBoolean).orElse(false)

val validatePublishEnvironment = tasks.register("validatePublishEnvironment") {
    group = "publishing"
    description = "Checks the environment variables required for a local release."

    doLast {
        if (!publishDryRun.get()) {
            if (!providers.environmentVariable("MODRINTH_TOKEN").isPresent) {
                println("MODRINTH_TOKEN not present")
            }
            if (!providers.environmentVariable("CURSEFORGE_TOKEN").isPresent) {
                println("CURSEFORGE_TOKEN not present")
            }
        }
    }
}

tasks.register("buildAll") {
    group = "build"
    description = "Builds every supported Minecraft version and loader."
    dependsOn(releaseTargets.map { ":$it:buildAndCollect" })
}

tasks.register("publishAll") {
    group = "publishing"
    description = "Publishes every supported Minecraft version and loader to Modrinth and CurseForge."
    dependsOn(validatePublishEnvironment)
    dependsOn(releaseTargets.map { ":$it:publishMods" })
}
