package band.kessoku.lib.mixin.registry.neoforge;

import band.kessoku.lib.api.registry.KessokuItem;
import band.kessoku.lib.impl.registry.neoforge.KessokuItemExtension;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Mixin(KessokuItem.class)
public abstract class KessokuItemMixin implements KessokuItemExtension {
    @Shadow
    public abstract EquipmentSlot getEquipmentSlot(LivingEntity entity, ItemStack stack);

    @Shadow
    public abstract boolean shadow$supportsEnchantment(ItemStack stack, RegistryEntry<Enchantment> enchantment);

    @Shadow
    public abstract boolean shadow$isPrimaryItemFor(ItemStack stack, RegistryEntry<Enchantment> enchantment);

    @Shadow
    public abstract ItemStack getRecipeRemainder(ItemStack stack);

    @Shadow
    public abstract boolean shadow$shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack);

    @Override
    public boolean supportsEnchantment(ItemStack stack, RegistryEntry<Enchantment> enchantment) {
        return this.shadow$supportsEnchantment(stack, enchantment);
    }

    @Override
    public boolean isPrimaryItemFor(ItemStack stack, RegistryEntry<Enchantment> enchantment) {
        return this.shadow$isPrimaryItemFor(stack, enchantment);
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return this.getRecipeRemainder(itemStack);
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return this.shadow$shouldCauseBlockBreakReset(oldStack, newStack);
    }
}
