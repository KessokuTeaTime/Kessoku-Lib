package band.kessoku.gradle.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.initialization.Settings;
import org.gradle.api.invocation.Gradle;
import org.gradle.api.plugins.PluginAware;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class KessokuGradlePlugin implements Plugin<PluginAware> {
    private static final List<String> NEO_GROUPS = List.of(
            "net.neoforged",
            "cpw.mods",
            "de.oceanlabs",
            "net.jodah",
            "org.mcmodlauncher"
    );

    @Override
    public void apply(@NotNull PluginAware target) {
        switch (target) {
            case Settings settings -> {
                additionalRepositories(settings.getDependencyResolutionManagement().getRepositories());

                settings.getGradle().getPluginManager().apply(KessokuGradlePlugin.class);
            }
            case Project project -> {
                project.getExtensions().create("kessoku", KesssokuExtension.class);

                additionalRepositories(project.getRepositories());
            }
            case Gradle gradle -> {
                return;
            }
            default ->
                    throw new IllegalArgumentException("Expected target to be a Project or Settings, but was a " + target.getClass());
        }
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
