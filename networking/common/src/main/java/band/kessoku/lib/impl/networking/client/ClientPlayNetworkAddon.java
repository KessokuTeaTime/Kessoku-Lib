package band.kessoku.lib.impl.networking.client;

import java.util.List;
import java.util.Objects;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.NetworkPhase;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Identifier;

import band.kessoku.lib.api.networking.client.C2SPlayChannelEvent;
import band.kessoku.lib.api.networking.client.ClientPlayConnectionEvent;
import band.kessoku.lib.api.networking.client.ClientPlayNetworking;
import band.kessoku.lib.api.networking.PacketSender;
import band.kessoku.lib.impl.networking.ChannelInfoHolder;

public final class ClientPlayNetworkAddon extends ClientCommonNetworkAddon<ClientPlayNetworking.PlayPayloadHandler<?>, ClientPlayNetworkHandler> {
    private final ContextImpl context;

    private static final Logger LOGGER = LogUtils.getLogger();

    public ClientPlayNetworkAddon(ClientPlayNetworkHandler handler, MinecraftClient client) {
        super(ClientNetworkingImpl.PLAY, handler.getConnection(), "ClientPlayNetworkAddon for " + handler.getProfile().getName(), handler, client);
        this.context = new ContextImpl(client, this);

        // Must register pending channels via lateinit
        this.registerPendingChannels((ChannelInfoHolder) this.connection, NetworkPhase.PLAY);
    }

    @Override
    protected void invokeInitEvent() {
        ClientPlayConnectionEvent.INIT.invoker().onPlayInit(this.handler, this.client);
    }

    @Override
    public void onServerReady() {
        try {
            ClientPlayConnectionEvent.JOIN.invoker().onPlayReady(this.handler, this, this.client);
        } catch (RuntimeException e) {
            LOGGER.error("Exception thrown while invoking ClientPlayConnectionEvents.JOIN", e);
        }

        // The client cannot send any packets, including `minecraft:register` until after GameJoinS2CPacket is received.
        this.sendInitialChannelRegistrationPacket();
        super.onServerReady();
    }

    @Override
    protected void receive(ClientPlayNetworking.PlayPayloadHandler<?> handler, CustomPayload payload) {
        this.client.execute(() -> {
            ((ClientPlayNetworking.PlayPayloadHandler) handler).receive(payload, context);
        });
    }

    // impl details
    @Override
    public Packet<?> createPacket(CustomPayload packet) {
        return ClientPlayNetworking.createC2SPacket(packet);
    }

    @Override
    protected void invokeRegisterEvent(List<Identifier> ids) {
        C2SPlayChannelEvent.REGISTER.invoker().onChannelRegister(this.handler, this, this.client, ids);
    }

    @Override
    protected void invokeUnregisterEvent(List<Identifier> ids) {
        C2SPlayChannelEvent.UNREGISTER.invoker().onChannelUnregister(this.handler, this, this.client, ids);
    }

    @Override
    protected void invokeDisconnectEvent() {
        ClientPlayConnectionEvent.DISCONNECT.invoker().onPlayDisconnect(this.handler, this.client);
    }

    private record ContextImpl(MinecraftClient client, PacketSender responseSender) implements ClientPlayNetworking.Context {
        private ContextImpl {
            Objects.requireNonNull(client, "client");
            Objects.requireNonNull(responseSender, "responseSender");
        }

        @Override
        public ClientPlayerEntity player() {
            return Objects.requireNonNull(client.player, "player");
        }
    }
}
