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
package band.kessoku.lib.registry.services;

import band.kessoku.lib.base.ModUtils;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public interface RegistryService {
    static RegistryService getInstance() {
        return ModUtils.loadService(RegistryService.class);
    }
    <V, T extends V> T register(Registry<V> registry, Identifier id, T entry);
}