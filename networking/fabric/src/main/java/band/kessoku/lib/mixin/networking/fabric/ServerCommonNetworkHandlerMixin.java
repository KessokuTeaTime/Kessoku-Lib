package band.kessoku.lib.mixin.networking.fabric;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.server.network.ServerCommonNetworkHandler;

import band.kessoku.lib.impl.networking.NetworkHandlerExtension;
import band.kessoku.lib.impl.networking.server.ServerConfigurationNetworkAddon;

@Mixin(ServerCommonNetworkHandler.class)
public abstract class ServerCommonNetworkHandlerMixin implements NetworkHandlerExtension {
    @Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
    private void handleCustomPayloadReceivedAsync(CustomPayloadC2SPacket packet, CallbackInfo ci) {
        final CustomPayload payload = packet.payload();

        boolean handled;

        if (kessokulib$getNetworkAddon() instanceof ServerConfigurationNetworkAddon addon) {
            handled = addon.handle(payload);
        } else {
            // Play should be handled in ServerPlayNetworkHandlerMixin
            throw new IllegalStateException("Unknown addon");
        }

        if (handled) {
            ci.cancel();
        }
    }

    @Inject(method = "onPong", at = @At("HEAD"))
    private void onPlayPong(CommonPongC2SPacket packet, CallbackInfo ci) {
        if (kessokulib$getNetworkAddon() instanceof ServerConfigurationNetworkAddon addon) {
            addon.onPong(packet.getParameter());
        }
    }
}
