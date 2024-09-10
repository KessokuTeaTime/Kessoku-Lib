package band.kessoku.lib.events.entity.mixin;

import band.kessoku.lib.events.entity.api.EntitySleepEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.player.PlayerEntity;

@Mixin(PlayerEntity.class)
abstract class PlayerEntityMixin {
    @Inject(method = "canResetTimeBySleeping", at = @At("RETURN"), cancellable = true)
    private void onIsSleepingLongEnough(CallbackInfoReturnable<Boolean> info) {
        if (info.getReturnValueZ()) {
            info.setReturnValue(EntitySleepEvent.ALLOW_RESETTING_TIME.invoker().allowResettingTime((PlayerEntity) (Object) this));
        }
    }
}
