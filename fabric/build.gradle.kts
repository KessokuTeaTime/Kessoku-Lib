dependencies {
    (rootProject.extra["getFabricModules"] as () -> List<Project>)().forEach {
        include(it)
    }
}