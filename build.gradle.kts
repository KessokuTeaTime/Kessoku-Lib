import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.plugins.ide.idea.model.IdeaModel
import java.nio.file.Path

plugins {
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("dev.architectury.loom") version "1.7-SNAPSHOT" apply false
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
}

val modules = listOf("kessoku-event-base")

allprojects {
    group = "band.kessokuteatime.kessokulib"
    apply<IdeaPlugin>()
    apply<JavaLibraryPlugin>()

    repositories {
        maven {
            name = "NeoForged"
            url = uri("https://maven.neoforged.net/releases")
        }
        maven {
            name = "Architectury"
            url = uri("https://maven.architectury.dev/")
        }
    }

    configure<IdeaModel> {
        module {
            isDownloadJavadoc = true
            isDownloadSources = true
        }
    }
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = JavaVersion.VERSION_21.toString()
        targetCompatibility = JavaVersion.VERSION_21.toString()
    }

    tasks.withType<ProcessResources> {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(mutableMapOf("version" to project.version))
        }
        filesMatching("META-INF/neoforge.mods.toml") {
            expand(mutableMapOf("version" to project.version))
        }
    }
}

subprojects {
    apply<BasePlugin>()
    apply(plugin = "dev.architectury.loom")
    if (project.parent != rootProject) {
        apply(plugin = "architectury-plugin")
    }
    if (project.parent == rootProject && !(project.name == "neo" || project.name == "fabric")) {
        apply(plugin = "architectury-plugin")
        architectury {
            minecraft = rootProject.libs.versions.minecraft.get()
        }
    }
    if (project.name == "common") {
        project.dependencies {
            "modImplementation"(rootProject.libs.fabric.loader)
        }
    }
    if (project.name == "fabric") {
        apply(plugin = "com.github.johnrengelman.shadow")
        project.dependencies {
            "modImplementation"(rootProject.libs.fabric.loader)
            "modImplementation"(rootProject.libs.fabric.api)
        }
    }
    if (project.name == "neo") {
        apply(plugin = "com.github.johnrengelman.shadow")
        project.dependencies {
            "neoForge"(rootProject.libs.neo)
        }
    }
    project.dependencies {
        val loom = project.extensions.getByType<LoomGradleExtensionAPI>()
        "minecraft"(rootProject.libs.minecraft)
        "mappings"(
            loom.layered {
                variantOf(rootProject.libs.yarn) { classifier("v2") }
                rootProject.libs.yarn.patch
            }
        )
    }
    extensions.getByType(BasePluginExtension::class.java).archivesName.set(project.parent!!.name)
    version =
        rootProject.libs.versions.mod.get() + "+" + (if (project.name == "neo") "neoforge" else project.name) + "." + rootProject.libs.versions.minecraft.get()

}

tasks.register("pre") {
    delete(file(Path.of(".", "artifacts")))
    modules.forEach {
        delete(file(Path.of(".", it, "common", "build", "libs")))
        delete(file(Path.of(".", it, "fabric", "build", "libs")))
        delete(file(Path.of(".", it, "neo", "build", "libs")))
    }
}

tasks.register("buildModules") {
    dependsOn(modules.flatMap { module ->
        listOf(
            ":$module:build",
            ":$module:common:moveFiles",
            ":$module:fabric:moveFiles",
            ":$module:neo:moveFiles"
        )
    })
}


tasks.register("bundleModules") {}

val pre: Task = rootProject.tasks.getByName("pre")
val buildModules: Task = rootProject.tasks.getByName("buildModules")
val bundleModules: Task = rootProject.tasks.getByName("bundleModules")

buildModules.dependsOn(pre)

project(":neo").tasks.getByName("build").dependsOn(buildModules)
project(":fabric").tasks.getByName("build").dependsOn(buildModules)
bundleModules.dependsOn(project(":neo").tasks.getByName("build"))
bundleModules.dependsOn(project(":fabric").tasks.getByName("build"))

// tasks.getByName(bundleModules.name).dependsOn(buildModules.name)
tasks.getByName("build").dependsOn(bundleModules)