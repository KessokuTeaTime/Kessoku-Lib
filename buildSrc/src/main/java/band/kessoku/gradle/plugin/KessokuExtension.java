package band.kessoku.gradle.plugin;

import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import net.fabricmc.loom.util.ModPlatform;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ModuleDependency;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.tasks.SourceSetContainer;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

public abstract class KessokuExtension {
    @Inject
    protected abstract Project getProject();

    public void library(String lib) {
        Project project = this.getProject();
        DependencyHandler dependencies = project.getDependencies();

        Dependency dependency = dependencies.project(Map.of(
                "path", lib,
                "configuration", "namedElements"
        ));
        dependencies.add("implementation", dependency);
    }

    public void testModules(List<String> names, String plat) {
        names.forEach(name -> testModule(name, plat));
    }

    public void modules(List<String> names, String plat) {
        names.forEach(name -> module(name, plat));
    }

    public void moduleIncludes(List<String> names, String plat) {
        names.forEach(name -> moduleInclude(name, plat));
    }

    public void testModule(String name, String plat) {
        Project project = this.getProject();
        DependencyHandler dependencies = project.getDependencies();

        Dependency dependency = dependencies.project(Map.of(
                "path", ":" + name + "-" + plat,
                "configuration", "namedElements"
        ));
        dependencies.add("testImplementation", dependency);
    }

    public void module(String name, String plat) {
        Project project = this.getProject();
        DependencyHandler dependencies = project.getDependencies();

        Dependency dependency = dependencies.project(Map.of(
                "path", ":" + name + "-" + plat,
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

    public void common(String name, ModPlatform platform) {
        Project project = this.getProject();
        DependencyHandler dependencies = project.getDependencies();

        ModuleDependency dependency = (ModuleDependency) dependencies.project(Map.of(
                "path", ":" + name + "-common",
                "configuration", "namedElements"
        ));
        dependency.setTransitive(false);
        dependencies.add("compileOnly", dependency);
        dependencies.add("runtimeOnly", dependency);
        dependencies.add("development" + platform.displayName(), dependency);
    }

    public void shadowBundle(String name, ModPlatform platform) {
        Project project = this.getProject();
        DependencyHandler dependencies = project.getDependencies();

        Dependency dependency = dependencies.project(Map.of(
                "path", ":" + name + "-common",
                "configuration", "transformProduction" + platform.displayName()
        ));
        dependencies.add("shade", dependency);
    }
}
