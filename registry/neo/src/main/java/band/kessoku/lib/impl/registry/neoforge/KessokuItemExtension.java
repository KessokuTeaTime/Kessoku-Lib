package band.kessoku.lib.impl.registry.neoforge;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;
import net.neoforged.neoforge.common.extensions.IItemExtension;

import javax.annotation.ParametersAreNonnullByDefault;

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
