package band.kessoku.lib.api.networking.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.CustomPayload;

import band.kessoku.lib.event.api.Event;
import band.kessoku.lib.api.networking.PacketSender;

/**
 * Offers access to events related to the connection to a server on a logical client.
 */
public final class ClientPlayConnectionEvent {
    /**
     * Event indicating a connection entered the PLAY state, ready for registering channel handlers.
     *
     * @see ClientPlayNetworking#registerReceiver(CustomPayload.Id, ClientPlayNetworking.PlayPayloadHandler)
     */
    public static final Event<Init> INIT = Event.of(inits -> (handler, client) -> {
        for (Init callback : inits) {
            callback.onPlayInit(handler, client);
        }
    });

    /**
     * An event for notification when the client play network handler is ready to send packets to the server.
     *
     * <p>At this stage, the network handler is ready to send packets to the server.
     * Since the client's local state has been set up.
     */
    public static final Event<Join> JOIN = Event.of(joins -> (handler, sender, client) -> {
        for (Join callback : joins) {
            callback.onPlayReady(handler, sender, client);
        }
    });

    /**
     * An event for the disconnection of the client play network handler.
     *
     * <p>No packets should be sent when this event is invoked.
     */
    public static final Event<Disconnect> DISCONNECT = Event.of(disconnects -> (handler, client) -> {
        for (Disconnect callback : disconnects) {
            callback.onPlayDisconnect(handler, client);
        }
    });

    @FunctionalInterface
    public interface Init {
        void onPlayInit(ClientPlayNetworkHandler handler, MinecraftClient client);
    }

    @FunctionalInterface
    public interface Join {
        void onPlayReady(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client);
    }

    @FunctionalInterface
    public interface Disconnect {
        void onPlayDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client);
    }
}
