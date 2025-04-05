package band.kessoku.scripts.gradle.plugin;

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

    protected static final List<String> MODULES = List.of(
            "base",
            "command",
            "config",
            "event",
            "entrypoint",
            "keybinding",
            "platform",
            "registry"
    );

    public List<String> getModuleList() {
        return MODULES;
    }

    public void testModules(List<String> names, PlatformIdentifier platform) {
        names.forEach(name -> {
            Project project = this.getProject();
            DependencyHandler dependencies = project.getDependencies();

            Dependency dependency = dependencies.project(Map.of(
                    "path", ":" + name + "-" + platform.id(),
                    "configuration", "namedElements"
            ));
            dependencies.add("testImplementation", dependency);
        });
    }

    public void modules(List<String> names, PlatformIdentifier platform) {
        names.forEach(name -> {
            Project project = this.getProject();
            DependencyHandler dependencies = project.getDependencies();

            Dependency dependency = dependencies.project(Map.of(
                    "path", ":" + name + "-" + platform.id(),
                    "configuration", "namedElements"
            ));
            dependencies.add("api", dependency);
            dependencies.add("implementation", dependency);

            LoomGradleExtensionAPI loom = project.getExtensions().getByType(LoomGradleExtensionAPI.class);
            loom.mods(mods -> mods.register("kessoku-" + name + "-" + platform.id(), settings -> {
                Project depProject = project.project(":" + name + "-" + platform.id());
                SourceSetContainer sourceSets = depProject.getExtensions().getByType(SourceSetContainer.class);
                settings.sourceSet(sourceSets.getByName("main"), depProject);
            }));
        });
    }

    public void moduleIncludes(List<String> names, PlatformIdentifier platform) {
        names.forEach(name -> {
            Project project = this.getProject();
            DependencyHandler dependencies = project.getDependencies();

            Dependency dependency = dependencies.project(Map.of(
                    "path", ":" + name + "-" + platform.id()
            ));
            dependencies.add("include", dependency);
        });
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
