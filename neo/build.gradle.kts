dependencies {
    neoForge(libs.neo)
    (rootProject.extra["getNeoModules"] as () -> List<Project>)().forEach {
        include(it)
    }
}