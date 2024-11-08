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
package band.kessoku.lib.impl.registry.neoforge;

import band.kessoku.lib.api.KessokuLib;
import band.kessoku.lib.api.base.neoforge.NeoEventUtils;
import band.kessoku.lib.api.registry.FuelRegistry;
import band.kessoku.lib.api.registry.KessokuRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Objects;

@Mod(KessokuRegistry.MOD_ID)
public final class KessokuRegistryNeoforge {
    public KessokuRegistryNeoforge(IEventBus modEventBus) {
        KessokuLib.loadModule(KessokuRegistry.class);
        NeoEventUtils.registerEvent(modEventBus, RegisterEvent.class, RegistryImpl::onRegister);
        NeoEventUtils.registerEvent(NeoForge.EVENT_BUS, FurnaceFuelBurnTimeEvent.class, event -> {
            final ItemStack stack = event.getItemStack();
            final RecipeType<?> recipeType = Objects.requireNonNullElse(event.getRecipeType(), RecipeType.SMELTING);

            int burnTime;
            if ((burnTime = FuelRegistry.of(recipeType).get(stack)) <= 0) {
                event.setBurnTime(burnTime);
            }
        });
    }
}
