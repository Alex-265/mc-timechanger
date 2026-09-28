import me.modmuss50.mpp.ReleaseType

plugins {
    id("net.neoforged.moddev") version "2.0.147"
    id("neoforge-mutex")
    id("me.modmuss50.mod-publish-plugin")
}

val modId = property("mod.id") as String
val modName = property("mod.name") as String
val modVersion = property("mod.version") as String
val modAuthor = property("mod.author") as String
val modLicense = property("mod.license") as String
val modDescription = property("mod.description") as String
val modHomepage = property("mod.homepage") as String
val modIssues = property("mod.issues") as String
val minecraftCompatibility = property("mod.mc_compat") as String
val neoForgeVersion = property("deps.neoforge") as String
val modrinthProject = property("publish.modrinth_project") as String
val curseForgeProject = property("publish.curseforge_project") as String
val curseForgeSlug = property("publish.curseforge_slug") as String

version = "$modVersion+${sc.current.version}"
base.archivesName = "$modId-neoforge"

val requiredJava = if (sc.current.parsed >= "26.1") JavaVersion.VERSION_25 else JavaVersion.VERSION_21
val compatibleVersions = sc.properties.rawOrNull("mod", "mc_releases")
    ?.asList().orEmpty().map(Any::toString)
val publishDryRun = providers.gradleProperty("publishDryRun").map(String::toBoolean).orElse(false)
val modrinthToken = providers.environmentVariable("MODRINTH_TOKEN")
val curseForgeToken = providers.environmentVariable("CURSEFORGE_TOKEN")

dependencies {
    compileOnly("io.github.llamalad7:mixinextras-common:0.3.5")
    annotationProcessor("io.github.llamalad7:mixinextras-common:0.3.5")
}

neoForge {
    version = neoForgeVersion

    mods {
        register("timechanger") {
            sourceSet(sourceSets.main.get())
        }
    }

    runs {
        register("client") {
            gameDirectory = rootProject.file("run")
            client()
        }
    }
}

java {
    withSourcesJar()
    sourceCompatibility = requiredJava
    targetCompatibility = requiredJava
    toolchain {
        vendor = JvmVendorSpec.ADOPTIUM
        languageVersion = JavaLanguageVersion.of(requiredJava.majorVersion)
    }
}

tasks {
    processResources {
        val props = mapOf(
            "id" to modId,
            "name" to modName,
            "version" to modVersion,
            "author" to modAuthor,
            "license" to modLicense,
            "description" to modDescription,
            "homepage" to modHomepage,
            "issues" to modIssues,
            "minecraft" to minecraftCompatibility,
        )
        inputs.properties(props)
        filesMatching("META-INF/neoforge.mods.toml") { expand(props) }
        filesMatching("timechanger.mixins.json") { expand("java" to "JAVA_${requiredJava.majorVersion}") }
        filesMatching("pack.mcmeta") { expand("name" to modName) }
        exclude("fabric.mod.json")
    }

    named("createMinecraftArtifacts") {
        dependsOn("stonecutterGenerate")
    }

    withType<Jar> {
        from(rootProject.file("LICENSE.txt")) { rename { "LICENSE-timechanger.txt" } }
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        description = "Builds and collects the NeoForge jars for this version."
        from(jar.flatMap { it.archiveFile }, named<Jar>("sourcesJar").flatMap { it.archiveFile })
        into(rootProject.layout.buildDirectory.dir("libs/$modVersion"))
    }

    matching { it.name == "publishModrinth" || it.name == "publishCurseforge" }.configureEach {
        dependsOn(rootProject.tasks.named("validatePublishEnvironment"))
    }
}

publishMods {
    dryRun = publishDryRun.get()
    file = tasks.jar.flatMap { it.archiveFile }
    additionalFiles.from(tasks.named<Jar>("sourcesJar").flatMap { it.archiveFile })
    displayName = "$modName $modVersion for NeoForge ${sc.current.version}"
    version = "$modVersion+${sc.current.version}-neoforge"
    changelog = providers.gradleProperty("releaseChangelog")
        .orElse("$modName $modVersion for Minecraft ${sc.current.version}")
    type = ReleaseType.STABLE
    modLoaders.add("neoforge")

    modrinth {
        projectId = modrinthProject
        accessToken = modrinthToken
        minecraftVersions.addAll(compatibleVersions)
    }

    curseforge {
        projectId = curseForgeProject
        projectSlug = curseForgeSlug
        accessToken = curseForgeToken
        minecraftVersions.addAll(compatibleVersions)
        javaVersions.add(requiredJava)
        client = true
        server = false
    }
}
