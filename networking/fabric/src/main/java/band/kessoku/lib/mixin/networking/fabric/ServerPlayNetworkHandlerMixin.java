package band.kessoku.lib.mixin.networking.fabric;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;

import net.fabricmc.fabric.impl.networking.NetworkHandlerExtensions;
import net.fabricmc.fabric.impl.networking.UntrackedNetworkHandler;
import net.fabricmc.fabric.impl.networking.server.ServerPlayNetworkAddon;

// We want to apply a bit earlier than other mods which may not use us in order to prevent refCount issues
@Mixin(value = ServerPlayNetworkHandler.class, priority = 999)
abstract class ServerPlayNetworkHandlerMixin extends ServerCommonNetworkHandler implements NetworkHandlerExtensions {
    @Unique
    private ServerPlayNetworkAddon addon;

    ServerPlayNetworkHandlerMixin(MinecraftServer server, ClientConnection connection, ConnectedClientData arg) {
        super(server, connection, arg);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initAddon(CallbackInfo ci) {
        this.addon = new ServerPlayNetworkAddon((ServerPlayNetworkHandler) (Object) this, connection, server);

        if (!(this instanceof UntrackedNetworkHandler)) {
            // A bit of a hack but it allows the field above to be set in case someone registers handlers during INIT event which refers to said field
            this.addon.lateInit();
        }
    }

    @Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
    private void handleCustomPayloadReceivedAsync(CustomPayloadC2SPacket packet, CallbackInfo ci) {
        if (getAddon().handle(packet.payload())) {
            ci.cancel();
        }
    }

    @Override
    public ServerPlayNetworkAddon getAddon() {
        return this.addon;
    }
}
