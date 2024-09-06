package band.kessoku.gradle.plugin;

import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.tasks.SourceSetContainer;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

public abstract class KesssokuExtension {
    @Inject
    protected abstract Project getProject();

    public void moduleImpls(List<String> names, String plat) {
        names.forEach(name -> moduleImpl(name, plat));
    }

    public void moduleIncludes(List<String> names, String plat) {
        names.forEach(name -> moduleInclude(name, plat));
    }

    public void moduleImpl(String name, String plat) {
        Project project = this.getProject();
        DependencyHandler dependencies = project.getDependencies();

        Dependency dependency = dependencies.project(Map.of(
                "path", ":" + name + "-common",
                "configuration", "namedElements"
        ));
        dependencies.add("api", dependency);

        LoomGradleExtensionAPI loom = project.getExtensions().getByType(LoomGradleExtensionAPI.class);
        loom.mods(mods -> mods.register("kessoku-" + name + "-" + plat, settings -> {
            Project depProject = project.project(":" + name + "-" + plat);
            SourceSetContainer sourceSets = depProject.getExtensions().getByType(SourceSetContainer.class);
            settings.sourceSet(sourceSets.getByName("main"), depProject);
        }));
    }

    public void moduleInclude(String name, String plat) {
        Project project = this.getProject();
        DependencyHandler dependencies = project.getDependencies();

        Dependency dependency = dependencies.project(Map.of(
                "path", ":" + name + "-" + plat,
                "configuration", "namedElements"
        ));
        dependencies.add("include", dependency);

        LoomGradleExtensionAPI loom = project.getExtensions().getByType(LoomGradleExtensionAPI.class);
        loom.mods(mods -> mods.register("kessoku-" + name + "-" + plat, settings -> {
            Project depProject = project.project(":" + name + "-" + plat);
            SourceSetContainer sourceSets = depProject.getExtensions().getByType(SourceSetContainer.class);
            settings.sourceSet(sourceSets.getByName("main"), depProject);
        }));
    }
}
