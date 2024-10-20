package band.kessoku.lib.api.networking.server;

import band.kessoku.lib.api.networking.PacketSender;
import band.kessoku.lib.event.api.Event;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;

/**
 * Offers access to events related to the connection to a client on a logical server while a client is in game.
 */
public final class ServerPlayConnectionEvent {
    /**
     * Event indicating a connection entered the PLAY state, ready for registering channel handlers.
     *
     * @see ServerPlayNetworking#registerReceiver(ServerPlayNetworkHandler, CustomPayload.Id, ServerPlayNetworking.PlayPayloadHandler)
     */
    public static final Event<Init> INIT = Event.of(inits -> (handler, server) -> {
        for (Init callback : inits) {
            callback.onPlayInit(handler, server);
        }
    });

    /**
     * An event for notification when the server play network handler is ready to send packets to the client.
     *
     * <p>At this stage, the network handler is ready to send packets to the client.
     */
    public static final Event<Join> JOIN = Event.of(joins -> (handler, sender, server) -> {
        for (Join callback : joins) {
            callback.onPlayReady(handler, sender, server);
        }
    });

    /**
     * An event for the disconnection of the server play network handler.
     *
     * <p>No packets should be sent when this event is invoked.
     */
    public static final Event<Disconnect> DISCONNECT = Event.of(disconnects -> (handler, server) -> {
        for (Disconnect callback : disconnects) {
            callback.onPlayDisconnect(handler, server);
        }
    });

    @FunctionalInterface
    public interface Init {
        void onPlayInit(ServerPlayNetworkHandler handler, MinecraftServer server);
    }

    @FunctionalInterface
    public interface Join {
        void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server);
    }

    @FunctionalInterface
    public interface Disconnect {
        void onPlayDisconnect(ServerPlayNetworkHandler handler, MinecraftServer server);
    }
}
