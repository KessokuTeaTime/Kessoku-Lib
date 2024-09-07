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
package band.kessoku.lib.registry.impl;

import band.kessoku.lib.registry.api.Registry;
import com.google.auto.service.AutoService;

import net.minecraft.util.Identifier;

@AutoService(Registry.class)
public class RegistryImpl implements Registry {
    @Override
    public <V, T extends V> T registerInternal(net.minecraft.registry.Registry<V> registry, Identifier id, T entry) {
        return net.minecraft.registry.Registry.register(registry, id, entry);
    }
}
