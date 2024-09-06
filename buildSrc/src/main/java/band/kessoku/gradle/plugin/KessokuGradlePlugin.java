package band.kessoku.gradle.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.jetbrains.annotations.NotNull;

public class KessokuGradlePlugin implements Plugin<Project> {

    @Override
    public void apply(@NotNull Project project) {
        project.getExtensions().create("kessoku", KesssokuExtension.class);

        additionalRepositories(project.getRepositories());
    }

    public void additionalRepositories(RepositoryHandler repositories) {
        repositories.maven(repo -> {
            repo.setName("NeoForge");
            repo.setUrl("https://maven.neoforged.net/releases/");
        });

        repositories.maven(repo -> {
            repo.setName("AmarokIce's Maven");
            repo.setUrl("http://maven.snowlyicewolf.club/");
            repo.setAllowInsecureProtocol(true);
            repo.mavenContent(context -> {
                context.includeGroup("club.someoneice");
            });
        });

        repositories.maven(repo -> {
            repo.setName("Jitpack Maven");
            repo.setUrl("https://jitpack.io");
            repo.mavenContent(context -> {
                context.includeGroupByRegex("com\\.github\\..*");
            });
        });
    }
}
