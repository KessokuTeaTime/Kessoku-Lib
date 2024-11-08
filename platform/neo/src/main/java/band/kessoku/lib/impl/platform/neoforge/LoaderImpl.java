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

import band.kessoku.lib.api.platform.Env;
import band.kessoku.lib.api.platform.Metadata;
import band.kessoku.lib.service.platform.LoaderService;
import com.google.auto.service.AutoService;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforgespi.language.IModInfo;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@AutoService(LoaderService.class)
public final class LoaderImpl implements LoaderService {
    private final Map<String, Metadata> modMatadataMap = new ConcurrentHashMap<>();

    @Override
    public Metadata getModMetadata(String modid) {
        return modMatadataMap.computeIfAbsent(modid, MetadataImpl::new);
    }

    @Override
    public boolean isFabric() {
        return false;
    }

    @Override
    public boolean isNeoForge() {
        return true;
    }

    @Override
    public Env getEnv() {
        return FMLLoader.getDist().isClient() ? Env.CLIENT : Env.SERVER;
    }

    @Override
    public Path getGameFolder() {
        return FMLPaths.GAMEDIR.get();
    }

    @Override
    public Path getConfigFolder() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public Path getModsFolder() {
        return FMLPaths.MODSDIR.get();
    }

    @Override
    public boolean isModLoaded(String id) {
        return ModList.get().isLoaded(id);
    }

    @Override
    public Collection<String> getModIds() {
        return ModList.get().getMods().parallelStream().map(IModInfo::getModId).toList();
    }

    @Override
    public Collection<? extends Metadata> getMods() {
        return ModList.get().getMods().parallelStream().map(MetadataImpl::new).toList();
    }

    @Override
    public boolean isDevEnv() {
        return !FMLLoader.isProduction();
    }
}
