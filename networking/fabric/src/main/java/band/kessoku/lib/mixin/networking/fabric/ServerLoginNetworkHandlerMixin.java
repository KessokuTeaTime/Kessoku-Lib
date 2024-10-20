package band.kessoku.lib.mixin.networking.fabric;

import com.mojang.authlib.GameProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.login.LoginQueryResponseC2SPacket;
import net.minecraft.network.packet.s2c.login.LoginQueryRequestS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;

import band.kessoku.lib.impl.networking.NetworkHandlerExtension;
import band.kessoku.lib.impl.networking.PacketCallbackListener;
import band.kessoku.lib.impl.networking.payload.PacketByteBufLoginQueryResponsePayload;
import band.kessoku.lib.impl.networking.server.ServerLoginNetworkAddon;

@Mixin(ServerLoginNetworkHandler.class)
abstract class ServerLoginNetworkHandlerMixin implements NetworkHandlerExtension, PacketCallbackListener {
    @Shadow
    protected abstract void tickVerify(GameProfile profile);

    @Unique
    private ServerLoginNetworkAddon addon;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initAddon(CallbackInfo ci) {
        this.addon = new ServerLoginNetworkAddon((ServerLoginNetworkHandler) (Object) this);
        // A bit of a hack but it allows the field above to be set in case someone registers handlers during INIT event which refers to said field
        this.addon.lateInit();
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerLoginNetworkHandler;tickVerify(Lcom/mojang/authlib/GameProfile;)V"))
    private void handlePlayerJoin(ServerLoginNetworkHandler instance, GameProfile profile) {
        // Do not accept the player, thereby moving into play stage until all login futures being waited on are completed
        if (this.addon.queryTick()) {
            this.tickVerify(profile);
        }
    }

    @Inject(method = "onQueryResponse", at = @At("HEAD"), cancellable = true)
    private void handleCustomPayloadReceivedAsync(LoginQueryResponseC2SPacket packet, CallbackInfo ci) {
        // Handle queries
        if (this.addon.handle(packet)) {
            ci.cancel();
        } else {
            if (packet.response() instanceof PacketByteBufLoginQueryResponsePayload response) {
                response.data().skipBytes(response.data().readableBytes());
            }
        }
    }

    @Redirect(method = "tickVerify", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getNetworkCompressionThreshold()I", ordinal = 0))
    private int removeLateCompressionPacketSending(MinecraftServer server) {
        return -1;
    }

    @Override
    public void kessokulib$sent(Packet<?> packet) {
        if (packet instanceof LoginQueryRequestS2CPacket) {
            this.addon.registerOutgoingPacket((LoginQueryRequestS2CPacket) packet);
        }
    }

    @Override
    public ServerLoginNetworkAddon kessokulib$getNetworkAddon() {
        return this.addon;
    }
}
