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
package band.kessoku.lib.service.registry;


import band.kessoku.lib.api.KessokuLib;
import band.kessoku.lib.api.registry.KessokuFuelRegistry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.tag.TagKey;

public interface FuelRegistryService {
    FuelRegistryService INSTANCE = KessokuLib.loadService(FuelRegistryService.class);

    void registerFuel(ItemConvertible item, int time);

    void registerFuel(TagKey<Item> tag, int time);

    void registerFuel(KessokuFuelRegistry.FuelTimeModifier modifier);
}
