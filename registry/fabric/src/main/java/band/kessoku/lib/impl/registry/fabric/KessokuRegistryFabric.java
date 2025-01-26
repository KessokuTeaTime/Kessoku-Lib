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
package band.kessoku.lib.impl.registry.fabric;

import band.kessoku.lib.api.KessokuLib;
import band.kessoku.lib.api.registry.KessokuRegistry;

import net.fabricmc.api.ModInitializer;

public final class KessokuRegistryFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        KessokuLib.loadModule(KessokuRegistry.class);
    }
}
