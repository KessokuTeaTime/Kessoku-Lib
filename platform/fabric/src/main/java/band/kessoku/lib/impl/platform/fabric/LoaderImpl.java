/*
 * Copyright (c) 2024, 2025 KessokuTeaTime
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
package band.kessoku.lib.impl.platform.fabric;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import band.kessoku.lib.api.platform.Env;
import band.kessoku.lib.api.platform.Metadata;
import band.kessoku.lib.service.platform.LoaderService;
import com.google.auto.service.AutoService;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;

@AutoService(LoaderService.class)
public final class LoaderImpl implements LoaderService {
    private final Map<String, Metadata> modMetadataMap = new ConcurrentHashMap<>();

    @Override
    public Metadata getModMetadata(String modid) {
        return modMetadataMap.computeIfAbsent(modid, MetadataImpl::new);
    }

    @Override
    public boolean isFabric() {
        return true;
    }

    @Override
    public boolean isNeoForge() {
        return false;
    }

    @Override
    public Env getEnv() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? Env.CLIENT : Env.SERVER;
    }

    @Override
    public Path getGameFolder() {
        return FabricLoader.getInstance().getGameDir();
    }

    @Override
    public Path getConfigFolder() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public Path getModsFolder() {
        return getGameFolder().resolve("mods");
    }

    @Override
    public boolean isModLoaded(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }

    @Override
    public Collection<String> getModIds() {
        return FabricLoader.getInstance().getAllMods().parallelStream().map(ModContainer::getMetadata).map(ModMetadata::getId).toList();
    }

    @Override
    public Collection<? extends Metadata> getMods() {
        return FabricLoader.getInstance().getAllMods().parallelStream().map(MetadataImpl::new).toList();
    }

    @Override
    public boolean isDevEnv() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
