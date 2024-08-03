package band.kessoku.lib.platform.impl;

import band.kessoku.lib.platform.api.ModData;
import band.kessoku.lib.platform.api.ModDependencyInfo;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ModDataImpl implements ModData {
    private final ModContainer modContainer;
    private final ModMetadata modMetadata;

    public ModDataImpl(String modid) {
        this.modContainer = FabricLoader.getInstance().getModContainer(modid).orElseThrow();
        this.modMetadata = modContainer.getMetadata();
    }

    @Override
    public String getModId() {
        return modMetadata.getId();
    }

    @Override
    public String getVersion() {
        return modMetadata.getVersion().getFriendlyString();
    }

    @Override
    public Collection<? extends ModDependencyInfo> getDependencies() {
        return modMetadata.getDependencies().stream().map(ModDependencyInfoImpl::new).collect(Collectors.toSet());
    }

    @Override
    public String getName() {
        return modMetadata.getName();
    }

    @Override
    public String getDescription() {
        return modMetadata.getDescription();
    }

    @Override
    public Collection<String> getAuthors() {
        return modMetadata.getAuthors().stream().map(Person::getName).collect(Collectors.toList());
    }

    @Override
    public Optional<String> getHomepage() {
        return modMetadata.getContact().get("homepage");
    }

    @Override
    public Optional<String> getSources() {
        return modMetadata.getContact().get("sources");
    }

    @Override
    public Optional<String> getIssueTracker() {
        return modMetadata.getContact().get("issues");
    }

    @Override
    public Collection<String> getLicense() {
        return modMetadata.getLicense();
    }

    @Override
    public Optional<String> getIconFile(int preferredSize) {
        return modMetadata.getIconPath(preferredSize);
    }

    @Override
    public List<Path> getRootPaths() {
        return modContainer.getRootPaths();
    }

    @Override
    public Optional<Path> findPath(String... path) {
        return modContainer.findPath(String.join("/", path));
    }

    public ModContainer getModContainer() {
        return modContainer;
    }

    public ModMetadata getModMetadata() {
        return modMetadata;
    }
}
