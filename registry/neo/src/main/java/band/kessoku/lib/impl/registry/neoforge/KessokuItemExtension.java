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
package band.kessoku.lib.impl.registry.neoforge;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import net.neoforged.neoforge.common.extensions.IItemExtension;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface KessokuItemExtension extends IItemExtension {
    @Override
    default boolean supportsEnchantment(ItemStack stack, RegistryEntry<Enchantment> enchantment) {
        throw new AssertionError("Implemented by band.kessoku.lib.mixin.registry.neoforge.KessokuItemMixin!");
    }

    @Override
    default boolean isPrimaryItemFor(ItemStack stack, RegistryEntry<Enchantment> enchantment) {
        throw new AssertionError("Implemented by band.kessoku.lib.mixin.registry.neoforge.KessokuItemMixin!");
    }

    @Override
    default ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        throw new AssertionError("Implemented by band.kessoku.lib.mixin.registry.neoforge.KessokuItemMixin!");
    }

    @Override
    default boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        throw new AssertionError("Implemented by band.kessoku.lib.mixin.registry.neoforge.KessokuItemMixin!");
    }
}
