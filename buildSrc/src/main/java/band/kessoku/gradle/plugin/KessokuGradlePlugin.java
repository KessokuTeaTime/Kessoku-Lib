package band.kessoku.gradle.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class KessokuGradlePlugin implements Plugin<Project> {
    private static final List<String> NEO_GROUPS = List.of(
            "net.neoforged",
            "cpw.mods",
            "de.oceanlabs",
            "net.jodah",
            "org.mcmodlauncher"
    );

    @Override
    public void apply(@NotNull Project project) {
        project.getExtensions().create("kessoku", KesssokuExtension.class);

        additionalRepositories(project.getRepositories());
    }

    public void additionalRepositories(RepositoryHandler repositories) {
        repositories.maven(repo -> {
            repo.setName("NeoForge");
            repo.setUrl("https://maven.neoforged.net/releases/");

            repo.content(descriptor -> {
                NEO_GROUPS.forEach(descriptor::includeGroupAndSubgroups);
            });

            repo.metadataSources(sources -> {
                sources.mavenPom();
                sources.ignoreGradleMetadataRedirection();
            });
        });

        repositories.maven(repo -> {
            repo.setName("AmarokIce's Maven");
            repo.setUrl("http://maven.snowlyicewolf.club/");
            repo.setAllowInsecureProtocol(true);
            repo.mavenContent(context -> {
                context.includeGroupByRegex("club\\.someoneice\\..*");
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
