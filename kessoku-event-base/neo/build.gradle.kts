import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.task.RemapJarTask
import java.nio.file.Path

architectury {
    platformSetupLoomIde()
    neoForge()
}

val shadowCommon by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

tasks {
    val shadowJarTask = named("shadowJar", ShadowJar::class)
    shadowJarTask {
        archiveClassifier.set("dev-shadow")
        configurations = listOf(shadowCommon)
    }
    "remapJar"(RemapJarTask::class) {
        dependsOn(shadowJarTask)
        inputFile = shadowJarTask.flatMap { it.archiveFile }
    }
}

val common: Configuration by configurations.creating {
    configurations.compileClasspath.get().extendsFrom(this)
    configurations.runtimeClasspath.get().extendsFrom(this)
    configurations["developmentNeoForge"].extendsFrom(this)
}

dependencies {
    common(project(path = ":kessoku-event-base:common", configuration = "namedElements")) {
        isTransitive = false
    }
    shadowCommon(project(path = ":kessoku-event-base:common", configuration = "transformProductionNeoForge"))
}


val moveFiles = tasks.register<Copy>("moveFiles") {
    dependsOn(tasks.getByName("build"))
    val filteredFiles = fileTree(file(Path.of(projectDir.absolutePath, "build", "libs"))) {
        include("**/*")
        exclude("**/*shadow*")
    }
    filteredFiles.files.forEach {
        from(it)
        into(Path.of(rootDir.path, "artifacts", "neo"))
    }
}