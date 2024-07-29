import java.nio.file.Path

architectury {
    common(listOf("fabric", "neoforge"))
}

val moveFiles = tasks.register<Copy>("moveFiles") {
    dependsOn(tasks.getByName("build"))
    val filteredFiles = fileTree(file(Path.of(projectDir.absolutePath, "build", "libs"))) {
        include("**/*")
        exclude("**/*transformProduction*")
    }
    filteredFiles.files.forEach {
        from(it)
        into(Path.of(rootDir.path, "artifacts", "common"))
    }
}
