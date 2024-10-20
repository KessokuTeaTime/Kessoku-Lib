package band.kessoku.lib.mixin.networking.fabric;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.ChannelHandlerContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkPhase;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.NetworkState;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Identifier;

import band.kessoku.lib.impl.networking.ChannelInfoHolder;
import band.kessoku.lib.impl.networking.NetworkHandlerExtension;
import band.kessoku.lib.impl.networking.PacketCallbackListener;

@Mixin(ClientConnection.class)
abstract class ClientConnectionMixin implements ChannelInfoHolder {
    @Shadow
    private PacketListener packetListener;

    @Unique
    private Map<NetworkPhase, Collection<Identifier>> kessokulib$playChannels;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initAddedFields(NetworkSide side, CallbackInfo ci) {
        this.kessokulib$playChannels = new ConcurrentHashMap<>();
    }

    @Inject(method = "sendImmediately", at = @At(value = "FIELD", target = "Lnet/minecraft/network/ClientConnection;packetsSentCounter:I"))
    private void checkPacket(Packet<?> packet, PacketCallbacks callback, boolean flush, CallbackInfo ci) {
        if (this.packetListener instanceof PacketCallbackListener) {
            ((PacketCallbackListener) this.packetListener).kessokulib$sent(packet);
        }
    }

    @Inject(method = "setPacketListener", at = @At("HEAD"))
    private void unwatchAddon(NetworkState<?> state, PacketListener listener, CallbackInfo ci) {
        if (this.packetListener instanceof NetworkHandlerExtension oldListener) {
            oldListener.kessokulib$getNetworkAddon().endSession();
        }
    }

    @Inject(method = "channelInactive", at = @At("HEAD"))
    private void disconnectAddon(ChannelHandlerContext channelHandlerContext, CallbackInfo ci) {
        if (packetListener instanceof NetworkHandlerExtension extension) {
            extension.kessokulib$getNetworkAddon().handleDisconnect();
        }
    }

    @Inject(method = "handleDisconnection", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/listener/PacketListener;onDisconnected(Lnet/minecraft/network/DisconnectionInfo;)V"))
    private void disconnectAddon(CallbackInfo ci) {
        if (packetListener instanceof NetworkHandlerExtension extension) {
            extension.kessokulib$getNetworkAddon().handleDisconnect();
        }
    }

    @Override
    public Collection<Identifier> kessokulib$getPendingChannelsNames(NetworkPhase state) {
        return this.kessokulib$playChannels.computeIfAbsent(state, (key) -> Collections.newSetFromMap(new ConcurrentHashMap<>()));
    }
}
