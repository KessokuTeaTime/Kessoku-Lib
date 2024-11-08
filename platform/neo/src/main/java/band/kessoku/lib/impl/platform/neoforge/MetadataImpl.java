/*
 * Copyright (c) 2024 KessokuTeaTime
 *
 * Licensed under the GNU Lesser General Pubic License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package band.kessoku.lib.impl.platform.neoforge;

import band.kessoku.lib.api.platform.DependencyInfo;
import band.kessoku.lib.api.platform.Metadata;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.moddiscovery.ModFileInfo;
import net.neoforged.neoforgespi.language.IModFileInfo;
import net.neoforged.neoforgespi.language.IModInfo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class MetadataImpl implements Metadata {
    private final ModContainer modContainer;
    private final IModInfo modInfo;

    public MetadataImpl(String modid) {
        this.modContainer = ModList.get().getModContainerById(modid).orElseThrow();
        this.modInfo = modContainer.getModInfo();
    }

    public MetadataImpl(IModInfo info) {
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
    public Collection<? extends DependencyInfo> getDependencies() {
        return modInfo.getDependencies().stream().map(DependencyInfoImpl::new).toList();
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
