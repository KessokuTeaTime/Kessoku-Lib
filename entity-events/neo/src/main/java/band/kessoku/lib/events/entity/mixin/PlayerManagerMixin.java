package band.kessoku.lib.events.entity.mixin;

import band.kessoku.lib.events.entity.api.ServerPlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(PlayerManager.class)
abstract class PlayerManagerMixin {
    @Inject(method = "respawnPlayer", at = @At("TAIL"))
    private void afterRespawn(ServerPlayerEntity oldPlayer, boolean alive, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayerEntity> cir) {
        ServerPlayerEvent.AFTER_RESPAWN.invoker().afterRespawn(oldPlayer, cir.getReturnValue(), alive);
    }
}
