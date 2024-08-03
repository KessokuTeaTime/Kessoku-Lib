package band.kessoku.lib.platform.impl;

import band.kessoku.lib.platform.api.ModData;
import band.kessoku.lib.platform.api.ModDependencyInfo;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.moddiscovery.ModFileInfo;
import net.neoforged.neoforgespi.language.IModFileInfo;
import net.neoforged.neoforgespi.language.IModInfo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class ModDataImpl implements ModData {
    private final ModContainer modContainer;
    private final IModInfo modInfo;

    public ModDataImpl(String modid) {
        this.modContainer = ModList.get().getModContainerById(modid).orElseThrow();
        this.modInfo = modContainer.getModInfo();
    }

    public ModDataImpl(IModInfo info) {
        this.modContainer = ModList.get().getModContainerById(info.getModId()).orElseThrow();
        this.modInfo = info;
    }

    @Override
    public String getModId() {
        return modInfo.getModId();
    }

    @Override
    public String getVersion() {
        return modInfo.getVersion().toString();
    }

    @Override
    public Collection<? extends ModDependencyInfo> getDependencies() {
        return modInfo.getDependencies().stream().map(ModDependencyInfoImpl::new).toList();
    }

    @Override
    public String getName() {
        return modInfo.getDisplayName();
    }

    @Override
    public String getDescription() {
        return modInfo.getDescription();
    }

    @Override
    public Collection<String> getAuthors() {
        Optional<String> optional = modInfo.getConfig().getConfigElement("authors").map(String::valueOf);
        return optional.isPresent() ? Collections.singleton(optional.get()) : Collections.emptyList();
    }

    @Override
    public Optional<String> getHomepage() {
        return modInfo.getConfig().getConfigElement("displayURL").map(String::valueOf);
    }

    @Override
    public Optional<String> getSources() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getIssueTracker() {
        IModFileInfo modFileInfo = modInfo.getOwningFile();
        if (modFileInfo instanceof ModFileInfo info) {
            return Optional.ofNullable(info.getIssueURL()).map(String::valueOf);
        }
        return Optional.empty();
    }

    @Override
    public Collection<String> getLicense() {
        return Collections.singleton(modInfo.getOwningFile().getLicense());
    }

    @Override
    public Optional<String> getIconFile(int preferredSize) {
        return modInfo.getLogoFile();
    }

    @Override
    public List<Path> getRootPaths() {
        return List.of(modInfo.getOwningFile().getFile().getSecureJar().getRootPath());
    }

    @Override
    public Optional<Path> findPath(String... path) {
        return Optional.of(modInfo.getOwningFile().getFile().findResource(path)).filter(Files::exists);
    }

    public ModContainer getModContainer() {
        return modContainer;
    }

    public IModInfo getModInfo() {
        return modInfo;
    }
}
