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
include("kessoku-event-base")
include("kessoku-event-base:common")
include("kessoku-event-base:fabric")
include("kessoku-event-base:neo")

project(":kessoku-event-base").name = "kessoku-event-base"