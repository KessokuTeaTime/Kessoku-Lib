package band.kessoku.lib.mixin.networking.fabric.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.network.packet.s2c.login.LoginQueryRequestS2CPacket;

import band.kessoku.lib.impl.networking.NetworkHandlerExtension;
import band.kessoku.lib.impl.networking.client.ClientLoginNetworkAddon;
import band.kessoku.lib.impl.networking.payload.PacketByteBufLoginQueryRequestPayload;

@Mixin(ClientLoginNetworkHandler.class)
abstract class ClientLoginNetworkHandlerMixin implements NetworkHandlerExtension {
    @Shadow
    @Final
    private MinecraftClient client;

    @Unique
    private ClientLoginNetworkAddon kessokulib$addon;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initAddon(CallbackInfo ci) {
        this.kessokulib$addon = new ClientLoginNetworkAddon((ClientLoginNetworkHandler) (Object) this, this.client);
        // A bit of a hack but it allows the field above to be set in case someone registers handlers during INIT event which refers to said field
        this.kessokulib$addon.lateInit();
    }

    @Inject(method = "onQueryRequest", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", remap = false, shift = At.Shift.AFTER), cancellable = true)
    private void handleQueryRequest(LoginQueryRequestS2CPacket packet, CallbackInfo ci) {
        if (packet.payload() instanceof PacketByteBufLoginQueryRequestPayload payload) {
            if (this.kessokulib$addon.handlePacket(packet)) {
                ci.cancel();
            } else {
                payload.data().skipBytes(payload.data().readableBytes());
            }
        }
    }

    @Override
    public ClientLoginNetworkAddon kessokulib$getNetworkAddon() {
        return this.kessokulib$addon;
    }
}
