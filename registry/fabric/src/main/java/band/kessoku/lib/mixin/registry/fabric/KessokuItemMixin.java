package band.kessoku.lib.mixin.registry.fabric;

import band.kessoku.lib.api.registry.KessokuItem;
import band.kessoku.lib.impl.registry.fabric.KessokuItemExtension;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

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
