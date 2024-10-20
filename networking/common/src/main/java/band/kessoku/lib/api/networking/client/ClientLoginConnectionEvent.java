package band.kessoku.lib.api.networking.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.util.Identifier;

import band.kessoku.lib.event.api.Event;

/**
 * Offers access to events related to the connection to a server on the client while the server is processing the client's login request.
 */
public final class ClientLoginConnectionEvent {
    /**
     * Event indicating a connection entered the LOGIN state, ready for registering query request handlers.
     * This event may be used by mods to prepare their client side state.
     * This event does not guarantee that a login attempt will be successful.
     *
     * @see ClientLoginNetworking#registerReceiver(Identifier, ClientLoginNetworking.LoginQueryRequestHandler)
     */
    public static final Event<Init> INIT = Event.of(inits -> (handler, client) -> {
        for (Init callback : inits) {
            callback.onLoginStart(handler, client);
        }
    });

    /**
     * An event for when the client has started receiving login queries.
     * A client can only start receiving login queries when a server has sent the first login query.
     * Vanilla servers will typically never make the client enter this login phase, but it is not a guarantee that the
     * connected server is a vanilla server since a modded server or proxy may have no login queries to send to the client
     * and therefore bypass the login query phase.
     * If this event is fired then it is a sign that a server is not a vanilla server or the server is behind a proxy which
     * is capable of handling login queries.
     *
     * <p>This event may be used to {@link ClientLoginNetworking.LoginQueryRequestHandler register login query handlers}
     * which may be used to send a response to a server.
     *
     * <p>No packets should be sent when this event is invoked.
     */
    public static final Event<QueryStart> QUERY_START = Event.of(queryStarts -> (handler, client) -> {
        for (QueryStart callback : queryStarts) {
            callback.onLoginQueryStart(handler, client);
        }
    });

    /**
     * An event for when the client's login process has ended due to disconnection.
     *
     * <p>No packets should be sent when this event is invoked.
     */
    public static final Event<Disconnect> DISCONNECT = Event.of(disconnects -> (handler, client) -> {
        for (Disconnect callback : disconnects) {
            callback.onLoginDisconnect(handler, client);
        }
    });

    /**
     * @see ClientLoginConnectionEvent#INIT
     */
    @FunctionalInterface
    public interface Init {
        void onLoginStart(ClientLoginNetworkHandler handler, MinecraftClient client);
    }

    /**
     * @see ClientLoginConnectionEvent#QUERY_START
     */
    @FunctionalInterface
    public interface QueryStart {
        void onLoginQueryStart(ClientLoginNetworkHandler handler, MinecraftClient client);
    }

    /**
     * @see ClientLoginConnectionEvent#DISCONNECT
     */
    @FunctionalInterface
    public interface Disconnect {
        void onLoginDisconnect(ClientLoginNetworkHandler handler, MinecraftClient client);
    }
}
