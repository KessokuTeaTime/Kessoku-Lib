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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import band.kessoku.lib.api.registry.KessokuFuelRegistry;
import band.kessoku.lib.service.registry.FuelRegistryService;
import com.google.auto.service.AutoService;

import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.tag.TagKey;

import net.fabricmc.fabric.api.registry.FuelRegistry;

@AutoService(FuelRegistryService.class)
public final class FuelRegistryImpl implements FuelRegistryService {
    public static final List<KessokuFuelRegistry.FuelTimeModifier> modifiers = new ArrayList<>();

    @Override
    public void registerFuel(ItemConvertible item, int time) {
        FuelRegistry.INSTANCE.add(item, time);
    }

    @Override
    public void registerFuel(TagKey<Item> tag, int time) {
        FuelRegistry.INSTANCE.add(tag, time);
    }

    @Override
    public void registerFuel(KessokuFuelRegistry.FuelTimeModifier modifier) {
        modifiers.add(modifier);
    }

    public static int test(ItemStack stack, RecipeType<?> recipeType) {
        final int baseTime = FuelRegistry.INSTANCE.get(stack.getItem());
        int time = baseTime;
        for (KessokuFuelRegistry.FuelTimeModifier modifier : modifiers) {
            Optional<Integer> optional = modifier.apply(stack, time, baseTime, recipeType);
            if (optional.isPresent()) time = optional.get();
        }
        return time;
    }
}
