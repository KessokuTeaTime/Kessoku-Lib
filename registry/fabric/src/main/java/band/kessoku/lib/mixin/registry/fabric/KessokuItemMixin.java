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
package band.kessoku.lib.mixin.registry.fabric;

import band.kessoku.lib.api.registry.KessokuItem;
import band.kessoku.lib.impl.registry.fabric.KessokuItemExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;

import net.fabricmc.fabric.api.item.v1.EnchantingContext;

@Mixin(KessokuItem.class)
public abstract class KessokuItemMixin implements KessokuItemExtension {
    @Shadow
    public abstract EquipmentSlot getEquipmentSlot(LivingEntity entity, ItemStack stack);

    @Shadow
    public abstract boolean supportsEnchantment(ItemStack stack, RegistryEntry<Enchantment> enchantment);

    @Shadow
    public abstract boolean isPrimaryItemFor(ItemStack stack, RegistryEntry<Enchantment> enchantment);

    @Shadow
    public abstract ItemStack shadow$getRecipeRemainder(ItemStack stack);

    @Shadow
    public abstract boolean shadow$shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack);

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, RegistryEntry<Enchantment> enchantment, EnchantingContext context) {
        return context == EnchantingContext.PRIMARY
                ? this.isPrimaryItemFor(stack, enchantment)
                : this.supportsEnchantment(stack, enchantment);
    }

    @Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        return this.shadow$getRecipeRemainder(stack);
    }

    @Override
    public boolean allowContinuingBlockBreaking(PlayerEntity player, ItemStack oldStack, ItemStack newStack) {
        return this.shadow$shouldCauseBlockBreakReset(oldStack, newStack);
    }
}
