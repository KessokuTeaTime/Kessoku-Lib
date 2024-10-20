package band.kessoku.lib.impl.networking.server;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkPhase;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import band.kessoku.lib.api.networking.PacketSender;
import band.kessoku.lib.api.networking.server.S2CPlayChannelEvent;
import band.kessoku.lib.api.networking.server.ServerPlayConnectionEvent;
import band.kessoku.lib.api.networking.server.ServerPlayNetworking;
import band.kessoku.lib.impl.networking.AbstractChanneledNetworkAddon;
import band.kessoku.lib.impl.networking.ChannelInfoHolder;
import band.kessoku.lib.impl.networking.NetworkingImpl;
import band.kessoku.lib.impl.networking.RegistrationPayload;

public final class ServerPlayNetworkAddon extends AbstractChanneledNetworkAddon<ServerPlayNetworking.PlayPayloadHandler<?>> {
    private final ServerPlayNetworkHandler handler;
    private final MinecraftServer server;
    private boolean sentInitialRegisterPacket;
    private final ServerPlayNetworking.Context context;

    public ServerPlayNetworkAddon(ServerPlayNetworkHandler handler, ClientConnection connection, MinecraftServer server) {
        super(ServerNetworkingImpl.PLAY, connection, "ServerPlayNetworkAddon for " + handler.player.getDisplayName());
        this.handler = handler;
        this.server = server;
        this.context = new ContextImpl(server, handler, this);

        // Must register pending channels via lateinit
        this.registerPendingChannels((ChannelInfoHolder) this.connection, NetworkPhase.PLAY);
    }

    @Override
    protected void invokeInitEvent() {
        ServerPlayConnectionEvent.INIT.invoker().onPlayInit(this.handler, this.server);
    }

    public void onClientReady() {
        ServerPlayConnectionEvent.JOIN.invoker().onPlayReady(this.handler, this, this.server);

        this.sendInitialChannelRegistrationPacket();
        this.sentInitialRegisterPacket = true;
    }

    @Override
    protected void receive(ServerPlayNetworking.PlayPayloadHandler<?> payloadHandler, CustomPayload payload) {
        this.server.execute(() -> {
            ((ServerPlayNetworking.PlayPayloadHandler) payloadHandler).receive(payload, ServerPlayNetworkAddon.this.context);
        });
    }

    // impl details

    @Override
    protected void schedule(Runnable task) {
        this.handler.player.server.execute(task);
    }

    @Override
    public Packet<?> createPacket(CustomPayload packet) {
        return ServerPlayNetworking.createS2CPacket(packet);
    }

    @Override
    protected void invokeRegisterEvent(List<Identifier> ids) {
        S2CPlayChannelEvent.REGISTER.invoker().onChannelRegister(this.handler, this, this.server, ids);
    }

    @Override
    protected void invokeUnregisterEvent(List<Identifier> ids) {
        S2CPlayChannelEvent.UNREGISTER.invoker().onChannelUnregister(this.handler, this, this.server, ids);
    }

    @Override
    protected void handleRegistration(Identifier channelName) {
        // If we can already send packets, immediately send the register packet for this channel
        if (this.sentInitialRegisterPacket) {
            RegistrationPayload registrationPayload = this.createRegistrationPayload(RegistrationPayload.REGISTER, Collections.singleton(channelName));

            if (registrationPayload != null) {
                this.sendPacket(registrationPayload);
            }
        }
    }

    @Override
    protected void handleUnregistration(Identifier channelName) {
        // If we can already send packets, immediately send the unregister packet for this channel
        if (this.sentInitialRegisterPacket) {
            RegistrationPayload registrationPayload = this.createRegistrationPayload(RegistrationPayload.UNREGISTER, Collections.singleton(channelName));

            if (registrationPayload != null) {
                this.sendPacket(registrationPayload);
            }
        }
    }

    @Override
    protected void invokeDisconnectEvent() {
        ServerPlayConnectionEvent.DISCONNECT.invoker().onPlayDisconnect(this.handler, this.server);
    }

    @Override
    protected boolean isReservedChannel(Identifier channelName) {
        return NetworkingImpl.isReservedCommonChannel(channelName);
    }

    private record ContextImpl(MinecraftServer server, ServerPlayNetworkHandler handler, PacketSender responseSender) implements ServerPlayNetworking.Context {
        private ContextImpl {
            Objects.requireNonNull(server, "server");
            Objects.requireNonNull(handler, "handler");
            Objects.requireNonNull(responseSender, "responseSender");
        }

        @Override
        public ServerPlayerEntity player() {
            return handler.getPlayer();
        }
    }
}
