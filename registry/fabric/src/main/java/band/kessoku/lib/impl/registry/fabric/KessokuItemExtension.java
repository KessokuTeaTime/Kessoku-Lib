package band.kessoku.lib.impl.registry.fabric;

import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;

public interface KessokuItemExtension extends FabricItem {
    @Override
    default boolean canBeEnchantedWith(ItemStack stack, RegistryEntry<Enchantment> enchantment, EnchantingContext context) {
        throw new AssertionError("Implemented by band.kessoku.lib.mixin.registry.fabric.KessokuItemMixin!");
    }

    @Override
    default ItemStack getRecipeRemainder(ItemStack stack) {
        throw new AssertionError("Implemented by band.kessoku.lib.mixin.registry.fabric.KessokuItemMixin!");
    }

    @Override
    default boolean allowContinuingBlockBreaking(PlayerEntity player, ItemStack oldStack, ItemStack newStack) {
        throw new AssertionError("Implemented by band.kessoku.lib.mixin.registry.fabric.KessokuItemMixin!");
    }
}
