package band.kessoku.lib.mixin.networking.fabric.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;

import band.kessoku.lib.impl.networking.NetworkHandlerExtension;
import band.kessoku.lib.impl.networking.client.ClientNetworkingImpl;
import band.kessoku.lib.impl.networking.client.ClientPlayNetworkAddon;

// We want to apply a bit earlier than other mods which may not use us in order to prevent refCount issues
@Mixin(value = ClientPlayNetworkHandler.class, priority = 999)
abstract class ClientPlayNetworkHandlerMixin extends ClientCommonNetworkHandler implements NetworkHandlerExtension {
    @Unique
    private ClientPlayNetworkAddon kessokulib$addon;

    protected ClientPlayNetworkHandlerMixin(MinecraftClient client, ClientConnection connection, ClientConnectionState connectionState) {
        super(client, connection, connectionState);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initAddon(CallbackInfo ci) {
        this.kessokulib$addon = new ClientPlayNetworkAddon((ClientPlayNetworkHandler) (Object) this, this.client);
        // A bit of a hack but it allows the field above to be set in case someone registers handlers during INIT event which refers to said field
        ClientNetworkingImpl.setClientPlayAddon(this.kessokulib$addon);
        this.kessokulib$addon.lateInit();
    }

    @Inject(method = "onGameJoin", at = @At("RETURN"))
    private void handleServerPlayReady(GameJoinS2CPacket packet, CallbackInfo ci) {
        this.kessokulib$addon.onServerReady();
    }

    @Override
    public ClientPlayNetworkAddon kessokulib$getNetworkAddon() {
        return this.kessokulib$addon;
    }
}
