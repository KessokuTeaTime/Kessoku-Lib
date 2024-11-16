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
package band.kessoku.lib.service.platform;

import java.nio.file.Path;
import java.util.Collection;

import band.kessoku.lib.api.KessokuLib;
import band.kessoku.lib.api.platform.Env;
import band.kessoku.lib.api.platform.Metadata;

public interface LoaderService {
    LoaderService INSTANCE = KessokuLib.loadService(LoaderService.class);

    Metadata getModMetadata(String modid);

    boolean isFabric();

    boolean isNeoForge();

    Env getEnv();

    Path getGameFolder();

    Path getConfigFolder();

    Path getModsFolder();

    boolean isModLoaded(String id);

    Collection<String> getModIds();

    Collection<? extends Metadata> getMods();

    boolean isDevEnv();
}
