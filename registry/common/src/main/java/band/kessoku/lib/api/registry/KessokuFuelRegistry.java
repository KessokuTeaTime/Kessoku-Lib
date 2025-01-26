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
package band.kessoku.lib.api.registry;

import java.util.Optional;

import band.kessoku.lib.service.registry.FuelRegistryService;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.tag.TagKey;

public final class KessokuFuelRegistry {
    private KessokuFuelRegistry() {
    }

    public static void registerFuel(ItemConvertible item, int time) {
        FuelRegistryService.INSTANCE.registerFuel(item, time);
    }

    public static void registerFuel(TagKey<Item> tag, int time) {
        FuelRegistryService.INSTANCE.registerFuel(tag, time);
    }

    @ApiStatus.Experimental
    public static void registerFuel(FuelTimeModifier modifier) {
        FuelRegistryService.INSTANCE.registerFuel(modifier);
    }

    @FunctionalInterface
    @ApiStatus.Experimental
    public interface FuelTimeModifier {
        Optional<Integer> apply(ItemStack stack, Integer currentTime, Integer baseTime, @Nullable RecipeType<?> recipeType);
    }
}
