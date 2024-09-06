package band.kessoku.gradle.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

public class KessokuGradlePlugin implements Plugin<Project> {

    @Override
    public void apply(@NotNull Project project) {
        project.getExtensions().create("kessoku", KesssokuExtension.class);

        project.getRepositories().maven(repo -> {
            repo.setName("NeoForge");
            repo.setUrl("https://maven.neoforged.net/releases/");
        });

        project.getRepositories().maven(repo -> {
            repo.setName("AmarokIce's Maven");
            repo.setUrl("http://maven.snowlyicewolf.club/");
            repo.setAllowInsecureProtocol(true);
            repo.mavenContent(context -> {
                context.includeGroup("club.someoneice.json");
            });
        });
    }
}
