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
package band.kessoku.lib.api.platform;

import java.nio.file.Path;
import java.util.Collection;

import band.kessoku.lib.service.platform.ModLoaderService;

public final class ModLoader {
    private ModLoader() {
    }

    public static ModData getModData(String modid) {
        return ModLoaderService.getInstance().getModData(modid);
    }

    public static boolean isFabric() {
        return ModLoaderService.getInstance().isFabric();
    }

    public static boolean isNeoForge() {
        return ModLoaderService.getInstance().isNeoForge();
    }

    public static Env getEnv() {
        return ModLoaderService.getInstance().getEnv();
    }

    public static Path getGameFolder() {
        return ModLoaderService.getInstance().getGameFolder();
    }

    public static Path getConfigFolder() {
        return ModLoaderService.getInstance().getConfigFolder();
    }

    public static Path getModsFolder() {
        return ModLoaderService.getInstance().getModsFolder();
    }

    public static boolean isModLoaded(String id) {
        return ModLoaderService.getInstance().isModLoaded(id);
    }

    public static Collection<String> getModIds() {
        return ModLoaderService.getInstance().getModIds();
    }

    public static Collection<? extends ModData> getMods() {
        return ModLoaderService.getInstance().getMods();
    }

    public static boolean isDevEnv() {
        return ModLoaderService.getInstance().isDevEnv();
    }
}
