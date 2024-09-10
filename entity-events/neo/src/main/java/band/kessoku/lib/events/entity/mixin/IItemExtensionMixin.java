package band.kessoku.lib.events.entity.mixin;

import band.kessoku.lib.events.entity.api.item.KessokuElytraItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IItemExtension.class)
public interface IItemExtensionMixin {
    @Inject(method = "canElytraFly", at = @At("HEAD"), cancellable = true)
    default void kessokulib$canElytraFly(ItemStack stack, LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this instanceof KessokuElytraItem);
    }
}
