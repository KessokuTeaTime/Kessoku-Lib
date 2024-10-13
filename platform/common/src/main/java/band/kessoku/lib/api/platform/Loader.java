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

import band.kessoku.lib.service.platform.LoaderService;

public final class Loader {
    private Loader() {
    }

    public static Metadata getModMetadata(String modid) {
        return LoaderService.getInstance().getModMetadata(modid);
    }

    public static boolean isFabric() {
        return LoaderService.getInstance().isFabric();
    }

    public static boolean isNeoForge() {
        return LoaderService.getInstance().isNeoForge();
    }

    public static Env getEnv() {
        return LoaderService.getInstance().getEnv();
    }

    public static Path getGameFolder() {
        return LoaderService.getInstance().getGameFolder();
    }

    public static Path getConfigFolder() {
        return LoaderService.getInstance().getConfigFolder();
    }

    public static Path getModsFolder() {
        return LoaderService.getInstance().getModsFolder();
    }

    public static boolean isModLoaded(String id) {
        return LoaderService.getInstance().isModLoaded(id);
    }

    public static Collection<String> getModIds() {
        return LoaderService.getInstance().getModIds();
    }

    public static Collection<? extends Metadata> getMods() {
        return LoaderService.getInstance().getMods();
    }

    public static boolean isDevEnv() {
        return LoaderService.getInstance().isDevEnv();
    }
}