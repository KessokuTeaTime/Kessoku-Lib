import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.plugins.ide.idea.model.IdeaModel

plugins {
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("dev.architectury.loom") version "1.7-SNAPSHOT" apply false
}

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


fun getNeoModules(): List<Project> {
    return subprojects.filter {
        it.name.contains("neo") && it != project(":neo")
    }
}

extra["getNeoModules"] = { getNeoModules() }

fun getFabricModules(): List<Project> {
    return subprojects.filter {
        it.name.contains("fabric") && it != project(":fabric")
    }
}

extra["getFabricModules"] = { getFabricModules() }

subprojects {
    apply(plugin = "dev.architectury.loom")
    apply<BasePlugin>()
    if (!project.parent?.name.equals("kessoku-lib")) {
        apply("architectury-plugin")
    }
    extensions.extraProperties["getNeoModules"] = { getNeoModules() }
    extensions.getByType(BasePluginExtension::class.java).archivesName.set(rootProject.name)
    version =
        rootProject.libs.versions.mod.get() + "+" + (if (project.name == "neo") "neoforge" else project.name) + "." + rootProject.libs.versions.minecraft.get()
    dependencies {
        val loom = project.extensions.getByType<LoomGradleExtensionAPI>()
        "minecraft"(rootProject.libs.minecraft)
        "mappings"(
            loom.layered {
                variantOf(rootProject.libs.yarn) { classifier("v2") }
                rootProject.libs.yarn.patch
            }
        )
    }
}

val buildModules = tasks.register("buildModules") {
    subprojects.forEach { p ->
        if (p.name != "neo" && p.name != "fabric" && p.name != "common") {
            val buildTask = p.tasks.getByName("build")
            buildTask.actions.forEach { it.execute(buildTask) }
        }
    }
}

val bundleModules = tasks.register("bundleModules") {
    val neoBuild = project(":neo").tasks.getByName("build")
    neoBuild.actions.forEach { it.execute(neoBuild) }
    val fabBuild = project(":fabric").tasks.getByName("build")
    fabBuild.actions.forEach { it.execute(fabBuild) }
}

tasks.getByName("bundleModules").dependsOn(buildModules.name)
tasks.getByName("build").dependsOn(buildModules.name)