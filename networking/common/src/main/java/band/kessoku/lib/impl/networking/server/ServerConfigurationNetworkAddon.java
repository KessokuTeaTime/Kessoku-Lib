package band.kessoku.lib.impl.networking.server;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.minecraft.network.NetworkPhase;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.common.CommonPingS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerConfigurationNetworkHandler;
import net.minecraft.util.Identifier;

import band.kessoku.lib.api.networking.PacketSender;
import band.kessoku.lib.api.networking.server.S2CConfigurationChannelEvent;
import band.kessoku.lib.api.networking.server.ServerConfigurationConnectionEvent;
import band.kessoku.lib.api.networking.server.ServerConfigurationNetworking;
import band.kessoku.lib.impl.networking.AbstractChanneledNetworkAddon;
import band.kessoku.lib.impl.networking.ChannelInfoHolder;
import band.kessoku.lib.impl.networking.NetworkingImpl;
import band.kessoku.lib.impl.networking.RegistrationPayload;
import band.kessoku.lib.mixin.networking.accessor.server.ServerCommonNetworkHandlerAccessor;

public final class ServerConfigurationNetworkAddon extends AbstractChanneledNetworkAddon<ServerConfigurationNetworking.ConfigurationPacketHandler<?>> {
    private final ServerConfigurationNetworkHandler handler;
    private final MinecraftServer server;
    private final ServerConfigurationNetworking.Context context;
    private RegisterState registerState = RegisterState.NOT_SENT;

    public ServerConfigurationNetworkAddon(ServerConfigurationNetworkHandler handler, MinecraftServer server) {
        super(ServerNetworkingImpl.CONFIG, ((ServerCommonNetworkHandlerAccessor) handler).getConnection(), "ServerConfigurationNetworkAddon for " + handler.getDebugProfile().getName());
        this.handler = handler;
        this.server = server;
        this.context = new ContextImpl(server, handler, this);

        // Must register pending channels via lateinit
        this.registerPendingChannels((ChannelInfoHolder) this.connection, NetworkPhase.CONFIGURATION);
    }

    @Override
    protected void invokeInitEvent() {
    }

    public void preConfig() {
        ServerConfigurationConnectionEvent.BEFORE_CONFIGURE.invoker().onSendConfiguration(handler, server);
    }

    public void config() {
        ServerConfigurationConnectionEvent.CONFIGURE.invoker().onSendConfiguration(handler, server);
    }

    public boolean startConfiguration() {
        if (this.registerState == RegisterState.NOT_SENT) {
            // Send the registration packet, followed by a ping
            this.sendInitialChannelRegistrationPacket();
            this.sendPacket(new CommonPingS2CPacket(0xFAB71C));

            this.registerState = RegisterState.SENT;

            // Cancel the configuration for now, the response from the ping or registration packet will continue.
            return true;
        }

        // We should have received a response
        assert registerState == RegisterState.RECEIVED || registerState == RegisterState.NOT_RECEIVED;
        return false;
    }

    @Override
    protected void receiveRegistration(boolean register, RegistrationPayload resolvable) {
        super.receiveRegistration(register, resolvable);

        if (register && registerState == RegisterState.SENT) {
            // We received the registration packet, thus we know this is a modded client, continue with configuration.
            registerState = RegisterState.RECEIVED;
            handler.sendConfigurations();
        }
    }

    public void onPong(int parameter) {
        if (registerState == RegisterState.SENT) {
            // We did not receive the registration packet, thus we think this is a vanilla client, continue with configuration.
            registerState = RegisterState.NOT_RECEIVED;
            handler.sendConfigurations();
        }
    }

    @Override
    protected void receive(ServerConfigurationNetworking.ConfigurationPacketHandler<?> handler, CustomPayload payload) {
        ((ServerConfigurationNetworking.ConfigurationPacketHandler) handler).receive(payload, this.context);
    }

    // impl details

    @Override
    protected void schedule(Runnable task) {
        this.server.execute(task);
    }

    @Override
    public Packet<?> createPacket(CustomPayload packet) {
        return ServerConfigurationNetworking.createS2CPacket(packet);
    }

    @Override
    protected void invokeRegisterEvent(List<Identifier> ids) {
        S2CConfigurationChannelEvent.REGISTER.invoker().onChannelRegister(this.handler, this, this.server, ids);
    }

    @Override
    protected void invokeUnregisterEvent(List<Identifier> ids) {
        S2CConfigurationChannelEvent.UNREGISTER.invoker().onChannelUnregister(this.handler, this, this.server, ids);
    }

    @Override
    protected void handleRegistration(Identifier channelName) {
        // If we can already send packets, immediately send the register packet for this channel
        if (this.registerState != RegisterState.NOT_SENT) {
            RegistrationPayload registrationPayload = this.createRegistrationPayload(RegistrationPayload.REGISTER, Collections.singleton(channelName));

            if (registrationPayload != null) {
                this.sendPacket(registrationPayload);
            }
        }
    }

    @Override
    protected void handleUnregistration(Identifier channelName) {
        // If we can already send packets, immediately send the unregister packet for this channel
        if (this.registerState != RegisterState.NOT_SENT) {
            RegistrationPayload registrationPayload = this.createRegistrationPayload(RegistrationPayload.UNREGISTER, Collections.singleton(channelName));

            if (registrationPayload != null) {
                this.sendPacket(registrationPayload);
            }
        }
    }

    @Override
    protected void invokeDisconnectEvent() {
        ServerConfigurationConnectionEvent.DISCONNECT.invoker().onConfigureDisconnect(handler, server);
    }

    @Override
    protected boolean isReservedChannel(Identifier channelName) {
        return NetworkingImpl.isReservedCommonChannel(channelName);
    }

    @Override
    public void sendPacket(Packet<?> packet, PacketCallbacks callback) {
        handler.send(packet, callback);
    }

    private enum RegisterState {
        NOT_SENT,
        SENT,
        RECEIVED,
        NOT_RECEIVED
    }

    public ChannelInfoHolder getChannelInfoHolder() {
        return (ChannelInfoHolder) ((ServerCommonNetworkHandlerAccessor) handler).getConnection();
    }

    private record ContextImpl(MinecraftServer server, ServerConfigurationNetworkHandler networkHandler, PacketSender responseSender) implements ServerConfigurationNetworking.Context {
        private ContextImpl {
            Objects.requireNonNull(server, "server");
            Objects.requireNonNull(networkHandler, "networkHandler");
            Objects.requireNonNull(responseSender, "responseSender");
        }
    }
}
