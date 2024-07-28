pluginManagement {
    repositories {
        maven { url = uri("https://maven.fabricmc.net/") }
        maven { url = uri("https://maven.architectury.dev/") }
        maven { url = uri("https://maven.minecraftforge.net/") }
        maven { url = uri("https://maven.neoforged.net/") }
        gradlePluginPortal()
    }
}

rootProject.name = "kessoku-lib"
include("neo")
include("fabric")