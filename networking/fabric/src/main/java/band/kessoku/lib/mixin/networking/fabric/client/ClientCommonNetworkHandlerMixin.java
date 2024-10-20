package band.kessoku.lib.mixin.networking.fabric.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;

import band.kessoku.lib.impl.networking.NetworkHandlerExtension;
import band.kessoku.lib.impl.networking.client.ClientConfigurationNetworkAddon;
import band.kessoku.lib.impl.networking.client.ClientPlayNetworkAddon;

@Mixin(ClientCommonNetworkHandler.class)
public abstract class ClientCommonNetworkHandlerMixin implements NetworkHandlerExtension {
    @Inject(method = "onCustomPayload(Lnet/minecraft/network/packet/s2c/common/CustomPayloadS2CPacket;)V", at = @At("HEAD"), cancellable = true)
    public void onCustomPayload(CustomPayloadS2CPacket packet, CallbackInfo ci) {
        final CustomPayload payload = packet.payload();
        boolean handled;

        if (this.kessokulib$getNetworkAddon() instanceof ClientPlayNetworkAddon addon) {
            handled = addon.handle(payload);
        } else if (this.kessokulib$getNetworkAddon() instanceof ClientConfigurationNetworkAddon addon) {
            handled = addon.handle(payload);
        } else {
            throw new IllegalStateException("Unknown network addon");
        }

        if (handled) {
            ci.cancel();
        }
    }
}
