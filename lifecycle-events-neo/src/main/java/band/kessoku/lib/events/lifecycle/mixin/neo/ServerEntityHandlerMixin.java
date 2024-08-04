package band.kessoku.lib.events.lifecycle.mixin.neo;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import band.kessoku.lib.events.lifecycle.api.ServerEntityEvent;

@Mixin(targets = "net/minecraft/server/world/ServerWorld$ServerEntityHandler")
public class ServerEntityHandlerMixin {
    // final synthetic Lnet/minecraft/server/world/ServerWorld; field_26936
    @SuppressWarnings("ShadowTarget")
    @Shadow
    @Final
    private ServerWorld field_26936;

    @Inject(method = "startTracking(Lnet/minecraft/entity/Entity;)V", at = @At("TAIL"))
    private void invokeEntityLoadEvent(Entity entity, CallbackInfo ci) {
        ServerEntityEvent.LOADED.invoker().onLoaded(entity, this.field_26936);
    }

    @Inject(method = "stopTracking(Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"))
    private void invokeEntityUnloadEvent(Entity entity, CallbackInfo info) {
        ServerEntityEvent.UNLOADED.invoker().onUnloaded(entity, this.field_26936);
    }
}
