package band.kessoku.lib.impl.networking.server;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

import org.jetbrains.annotations.Nullable;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.login.LoginQueryResponseC2SPacket;
import net.minecraft.network.packet.s2c.login.LoginCompressionS2CPacket;
import net.minecraft.network.packet.s2c.login.LoginQueryRequestS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import band.kessoku.lib.api.networking.LoginPacketSender;
import band.kessoku.lib.api.networking.PacketByteBufHelper;
import band.kessoku.lib.api.networking.server.ServerLoginConnectionEvent;
import band.kessoku.lib.api.networking.server.ServerLoginNetworking;
import band.kessoku.lib.impl.networking.AbstractNetworkAddon;
import band.kessoku.lib.impl.networking.payload.PacketByteBufLoginQueryRequestPayload;
import band.kessoku.lib.impl.networking.payload.PacketByteBufLoginQueryResponsePayload;
import band.kessoku.lib.mixin.networking.accessor.server.ServerLoginNetworkHandlerAccessor;

public final class ServerLoginNetworkAddon extends AbstractNetworkAddon<ServerLoginNetworking.LoginQueryResponseHandler> implements LoginPacketSender {
    private final ClientConnection connection;
    private final ServerLoginNetworkHandler handler;
    private final MinecraftServer server;
    private final QueryIdFactory queryIdFactory;
    private final Collection<Future<?>> waits = new ConcurrentLinkedQueue<>();
    private final Map<Integer, Identifier> channels = new ConcurrentHashMap<>();
    private boolean firstQueryTick = true;

    public ServerLoginNetworkAddon(ServerLoginNetworkHandler handler) {
        super(ServerNetworkingImpl.LOGIN, "ServerLoginNetworkAddon for " + handler.getConnectionInfo());
        this.connection = ((ServerLoginNetworkHandlerAccessor) handler).getConnection();
        this.handler = handler;
        this.server = ((ServerLoginNetworkHandlerAccessor) handler).getServer();
        this.queryIdFactory = QueryIdFactory.create();
    }

    @Override
    protected void invokeInitEvent() {
        ServerLoginConnectionEvent.INIT.invoker().onLoginInit(handler, this.server);
    }

    // return true if no longer ticks query
    public boolean queryTick() {
        if (this.firstQueryTick) {
            // Send the compression packet now so clients receive compressed login queries
            this.sendCompressionPacket();

            ServerLoginConnectionEvent.QUERY_START.invoker().onLoginStart(this.handler, this.server, this, this.waits::add);
            this.firstQueryTick = false;
        }

        AtomicReference<Throwable> error = new AtomicReference<>();
        this.waits.removeIf(future -> {
            if (!future.isDone()) {
                return false;
            }

            try {
                future.get();
            } catch (ExecutionException ex) {
                Throwable caught = ex.getCause();
                error.getAndUpdate(oldEx -> {
                    if (oldEx == null) {
                        return caught;
                    }

                    oldEx.addSuppressed(caught);
                    return oldEx;
                });
            } catch (InterruptedException | CancellationException ignored) {
                // ignore
            }

            return true;
        });

        return this.channels.isEmpty() && this.waits.isEmpty();
    }

    private void sendCompressionPacket() {
        // Compression is not needed for local transport
        if (this.server.getNetworkCompressionThreshold() >= 0 && !this.connection.isLocal()) {
            this.connection.send(new LoginCompressionS2CPacket(this.server.getNetworkCompressionThreshold()),
                    PacketCallbacks.always(() -> connection.setCompressionThreshold(server.getNetworkCompressionThreshold(), true))
            );
        }
    }

    /**
     * Handles an incoming query response during login.
     *
     * @param packet the packet to handle
     * @return true if the packet was handled
     */
    public boolean handle(LoginQueryResponseC2SPacket packet) {
        PacketByteBufLoginQueryResponsePayload response = (PacketByteBufLoginQueryResponsePayload) packet.response();
        return handle(packet.queryId(), response == null ? null : response.data());
    }

    private boolean handle(int queryId, @Nullable PacketByteBuf originalBuf) {
        this.logger.debug("Handling inbound login query with id {}", queryId);
        Identifier channel = this.channels.remove(queryId);

        if (channel == null) {
            this.logger.warn("Query ID {} was received but no query has been associated in {}!", queryId, this.connection);
            return false;
        }

        boolean understood = originalBuf != null;
        @Nullable ServerLoginNetworking.LoginQueryResponseHandler handler = this.getHandler(channel);

        if (handler == null) {
            return false;
        }

        PacketByteBuf buf = understood ? PacketByteBufHelper.slice(originalBuf) : PacketByteBufHelper.empty();

        try {
            handler.receive(this.server, this.handler, understood, buf, this.waits::add, this);
        } catch (Throwable ex) {
            this.logger.error("Encountered exception while handling in channel \"{}\"", channel, ex);
            throw ex;
        }

        return true;
    }

    @Override
    public Packet<?> createPacket(CustomPayload packet) {
        throw new UnsupportedOperationException("Cannot send CustomPayload during login");
    }

    @Override
    public Packet<?> createPacket(Identifier channelName, PacketByteBuf buf) {
        int queryId = this.queryIdFactory.nextId();
        return new LoginQueryRequestS2CPacket(queryId, new PacketByteBufLoginQueryRequestPayload(channelName, buf));
    }

    @Override
    public void sendPacket(Packet<?> packet, PacketCallbacks callback) {
        Objects.requireNonNull(packet, "Packet cannot be null");

        this.connection.send(packet, callback);
    }

    @Override
    public void disconnect(Text disconnectReason) {
        Objects.requireNonNull(disconnectReason, "Disconnect reason cannot be null");

        this.connection.disconnect(disconnectReason);
    }

    public void registerOutgoingPacket(LoginQueryRequestS2CPacket packet) {
        this.channels.put(packet.queryId(), packet.payload().id());
    }

    @Override
    protected void handleRegistration(Identifier channelName) {
    }

    @Override
    protected void handleUnregistration(Identifier channelName) {
    }

    @Override
    protected void invokeDisconnectEvent() {
        ServerLoginConnectionEvent.DISCONNECT.invoker().onLoginDisconnect(this.handler, this.server);
    }

    @Override
    protected boolean isReservedChannel(Identifier channelName) {
        return false;
    }
}