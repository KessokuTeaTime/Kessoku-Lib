package band.kessoku.lib.events.lifecycle.mixin.neo.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

import band.kessoku.lib.events.lifecycle.api.client.ClientEntityEvent;

@Mixin(targets = "net/minecraft/client/world/ClientWorld$ClientEntityHandler")
public class ClientEntityHandlerMixin {
    // final synthetic Lnet/minecraft/client/world/ClientWorld; field_27735
    @SuppressWarnings("ShadowTarget")
    @Shadow
    @Final
    private ClientWorld field_27735;

    // Call our load event after vanilla has loaded the entity
    @Inject(method = "startTracking(Lnet/minecraft/entity/Entity;)V", at = @At("TAIL"))
    private void invokeLoadEntity(Entity entity, CallbackInfo ci) {
        ClientEntityEvent.LOADED.invoker().onLoaded(entity, this.field_27735);
    }

    // Call our unload event before vanilla does.
    @Inject(method = "stopTracking(Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"))
    private void invokeUnloadEntity(Entity entity, CallbackInfo ci) {
        ClientEntityEvent.UNLOADED.invoker().onUnloaded(entity, this.field_27735);
    }
}
