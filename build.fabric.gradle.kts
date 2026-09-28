import me.modmuss50.mpp.ReleaseType

plugins {
    id("dev.kikugie.loom-back-compat")
    id("me.modmuss50.mod-publish-plugin")
}

val modId = property("mod.id") as String
val modName = property("mod.name") as String
val modVersion = property("mod.version") as String
val modAuthor = property("mod.author") as String
val modLicense = property("mod.license") as String
val modDescription = property("mod.description") as String
val modHomepage = property("mod.homepage") as String
val modSources = property("mod.sources") as String
val modIssues = property("mod.issues") as String
val minecraftCompatibility = property("mod.mc_compat") as String
val fabricLoader = property("deps.fabric_loader") as String
val fabricApi = property("deps.fabric_api") as String
val modMenu = property("deps.modmenu") as String
val modrinthProject = property("publish.modrinth_project") as String
val curseForgeProject = property("publish.curseforge_project") as String
val curseForgeSlug = property("publish.curseforge_slug") as String

version = "$modVersion+${sc.current.version}"
base.archivesName = "$modId-fabric"

val requiredJava = if (sc.current.parsed >= "26.1") JavaVersion.VERSION_25 else JavaVersion.VERSION_21
val compatibleVersions = sc.properties.rawOrNull("mod", "mc_releases")
    ?.asList().orEmpty().map(Any::toString)
val publishDryRun = providers.gradleProperty("publishDryRun").map(String::toBoolean).orElse(false)
val modrinthToken = providers.environmentVariable("MODRINTH_TOKEN")
val curseForgeToken = providers.environmentVariable("CURSEFORGE_TOKEN")

repositories {
    maven("https://maven.terraformersmc.com/releases/") { name = "Terraformers" }
}

dependencies {
    minecraft("com.mojang:minecraft:${sc.current.version}")
    loomx.applyMojangMappings()

    modImplementation("net.fabricmc:fabric-loader:$fabricLoader")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApi")
    modCompileOnly("com.terraformersmc:modmenu:$modMenu")

    compileOnly("io.github.llamalad7:mixinextras-common:0.3.5")
    annotationProcessor("io.github.llamalad7:mixinextras-common:0.3.5")
}

loom {
    fabricModJsonPath = rootProject.file("src/main/resources/fabric.mod.json")

    runConfigs.all {
        preferGradleTask = true
        generateRunConfig = true
        runDirectory = rootProject.file("run")
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
            "sources" to modSources,
            "issues" to modIssues,
            "minecraft" to minecraftCompatibility,
            "fabric_loader" to fabricLoader,
            "java" to requiredJava.majorVersion,
        )
        inputs.properties(props)
        filesMatching("fabric.mod.json") { expand(props) }
        filesMatching("timechanger.mixins.json") { expand("java" to "JAVA_${requiredJava.majorVersion}") }
        filesMatching("pack.mcmeta") { expand("name" to modName) }
        exclude("META-INF/neoforge.mods.toml")
    }

    withType<Jar> {
        from(rootProject.file("LICENSE.txt")) { rename { "LICENSE-timechanger.txt" } }
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        description = "Builds and collects the Fabric jars for this version."
        from(loomx.modJar.flatMap { it.archiveFile }, loomx.modSourcesJar.flatMap { it.archiveFile })
        into(rootProject.layout.buildDirectory.dir("libs/$modVersion"))
    }

    matching { it.name == "publishModrinth" || it.name == "publishCurseforge" }.configureEach {
        dependsOn(rootProject.tasks.named("validatePublishEnvironment"))
    }
}

publishMods {
    dryRun = publishDryRun.get()
    file = loomx.modJar.flatMap { it.archiveFile }
    additionalFiles.from(loomx.modSourcesJar.flatMap { it.archiveFile })
    displayName = "$modName $modVersion for Fabric ${sc.current.version}"
    version = "$modVersion+${sc.current.version}-fabric"
    changelog = providers.gradleProperty("releaseChangelog")
        .orElse("$modName $modVersion for Minecraft ${sc.current.version}")
    type = ReleaseType.STABLE
    modLoaders.add("fabric")

    modrinth {
        projectId = modrinthProject
        accessToken = modrinthToken
        minecraftVersions.addAll(compatibleVersions)
        requires("fabric-api")
        optional("modmenu")
    }

    curseforge {
        projectId = curseForgeProject
        projectSlug = curseForgeSlug
        accessToken = curseForgeToken
        minecraftVersions.addAll(compatibleVersions)
        javaVersions.add(requiredJava)
        client = true
        server = false
        requires("fabric-api")
        optional("modmenu")
    }
}
