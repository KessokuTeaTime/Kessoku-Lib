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
package band.kessoku.lib.api.registry;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import band.kessoku.lib.api.platform.Loader;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;

public class KessokuItem extends Item {
    public KessokuItem(Settings settings) {
        super(settings);
    }

    /**
     * Override this to set a non-default armor slot for an ItemStack, but <em>do
     * not use this to get the armor slot of said stack; for that, use
     * {@link LivingEntity#getPreferredEquipmentSlot(ItemStack)}..</em>
     *
     * @param stack the ItemStack
     * @return the armor slot of the ItemStack, or {@code null} to let the default
     * vanilla logic as per {@code LivingEntity.getSlotForItemStack(stack)}
     * decide
     */
    @Nullable
    public EquipmentSlot getEquipmentSlot(LivingEntity entity, ItemStack stack) {
        return null;
    }

    public boolean supportsEnchantment(ItemStack stack, RegistryEntry<Enchantment> enchantment) {
        return stack.isOf(Items.ENCHANTED_BOOK) || enchantment.value().isSupportedItem(stack);
    }

    @ApiStatus.OverrideOnly
    public boolean isPrimaryItemFor(ItemStack stack, RegistryEntry<Enchantment> enchantment) {
        if (stack.getItem() == Items.BOOK) {
            return true;
        }
        Optional<RegistryEntryList<Item>> primaryItems = enchantment.value().definition().primaryItems();
        return this.supportsEnchantment(stack, enchantment) && (primaryItems.isEmpty() || stack.isIn(primaryItems.get()));
    }

    /**
     * Returns a leftover item stack after {@code stack} is consumed in a recipe.
     * (This is also known as "recipe remainder".)
     * For example, using a lava bucket in a furnace as fuel will leave an empty bucket.
     *
     * <p>Here is an example for a recipe remainder that increments the item's damage.
     *
     * <pre>{@code
     *  if (stack.getDamage() < stack.getMaxDamage() - 1) {
     *  	ItemStack moreDamaged = stack.copy();
     *  	moreDamaged.setDamage(stack.getDamage() + 1);
     *  	return moreDamaged;
     *  }
     *
     *  return ItemStack.EMPTY;
     * }</pre>
     *
     *
     * <p>This is a stack-aware version of {@link Item#getRecipeRemainder()}.
     *
     * <p>Note that simple item remainders can also be set via {@link Item.Settings#recipeRemainder(Item)}.
     *
     * <p>If you want to get a remainder for a stack,
     * is recommended to use the stack version of this method.
     *
     * @param stack the consumed {@link ItemStack}
     * @return the leftover item stack
     */
    public ItemStack getRecipeRemainder(ItemStack stack) {
        return this.hasRecipeRemainder() ? this.getRecipeRemainder().getDefaultStack() : ItemStack.EMPTY;
    }

    /**
     * @implNote this method is called on different places
     * You should
     */
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        if (Loader.isFabric()) return false;
        // Next code is copied from NeoForge under LGPL-2.1
        // Fix MC-176559 mending resets mining progress / breaking animation
        if (!newStack.isOf(oldStack.getItem())) return true;

        if (!newStack.isDamageable() || !oldStack.isDamageable()) {
            return !ItemStack.areItemsAndComponentsEqual(newStack, oldStack);
        }

        ComponentMap newComponents = newStack.getComponents();
        ComponentMap oldComponents = oldStack.getComponents();

        if (newComponents.isEmpty() || oldComponents.isEmpty())
            return !(newComponents.isEmpty() && oldComponents.isEmpty());

        Set<ComponentType<?>> newKeys = new HashSet<>(newComponents.getTypes());
        Set<ComponentType<?>> oldKeys = new HashSet<>(oldComponents.getTypes());

        newKeys.remove(DataComponentTypes.DAMAGE);
        oldKeys.remove(DataComponentTypes.DAMAGE);

        if (!newKeys.equals(oldKeys)) return true;

        return !newKeys.stream().allMatch(key -> Objects.equals(newComponents.get(key), oldComponents.get(key)));
    }
}
