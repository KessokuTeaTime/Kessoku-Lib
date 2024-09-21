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
package band.kessoku.lib.platform.services;

import band.kessoku.lib.base.ModUtils;
import band.kessoku.lib.platform.api.Env;
import band.kessoku.lib.platform.api.ModData;

import java.nio.file.Path;
import java.util.Collection;

public interface ModLoaderService {
    static ModLoaderService getInstance() {
        return ModUtils.loadService(ModLoaderService.class);
    }

    ModData getModData(String modid);
    boolean isFabric();
    boolean isNeoForge();
    Env getEnv();
    Path getGameFolder();
    Path getConfigFolder();
    Path getModsFolder();
    boolean isModLoaded(String id);
    Collection<String> getModIds();
    Collection<? extends ModData> getMods();
    boolean isDevEnv();
}
