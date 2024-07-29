import java.nio.file.Path

dependencies {
    include(files(fileTree(Path.of(rootDir.absolutePath, "artifacts", "fabric")) { include("**/*") }))
}