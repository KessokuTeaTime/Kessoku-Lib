package band.kessoku.lib.api.networking.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientConfigurationNetworkHandler;
import net.minecraft.network.packet.CustomPayload;

import band.kessoku.lib.event.api.Event;

/**
 * Offers access to events related to the configuration connection to a server on a logical client.
 */
public final class ClientConfigurationConnectionEvent {
    /**
     * Event indicating a connection entering the CONFIGURATION state, ready for registering channel handlers.
     *
     * <p>No packets should be sent when this event is invoked.
     *
     * @see ClientConfigurationNetworking#registerReceiver(CustomPayload.Id, ClientConfigurationNetworking.ConfigurationPayloadHandler)
     */
    public static final Event<Init> INIT = Event.of(inits -> (handler, client) -> {
        for (ClientConfigurationConnectionEvent.Init callback : inits) {
            callback.onConfigurationInit(handler, client);
        }
    });

    /**
     * An event called after the connection has been initialized and is ready to start sending and receiving configuration packets.
     *
     * <p>Packets may be sent during this event.
     */
    public static final Event<Start> START = Event.of(starts -> (handler, client) -> {
        for (ClientConfigurationConnectionEvent.Start callback : starts) {
            callback.onConfigurationStart(handler, client);
        }
    });

    /**
     * An event called after the ReadyS2CPacket has been received, just before switching to the PLAY state.
     *
     * <p>No packets should be sent when this event is invoked.
     */
    public static final Event<Complete> COMPLETE = Event.of(completes -> (handler, client) -> {
        for (ClientConfigurationConnectionEvent.Complete callback : completes) {
            callback.onConfigurationComplete(handler, client);
        }
    });

    /**
     * An event for the disconnection of the client configuration network handler.
     *
     * <p>No packets should be sent when this event is invoked.
     */
    public static final Event<Disconnect> DISCONNECT = Event.of(disconnects -> (handler, client) -> {
        for (ClientConfigurationConnectionEvent.Disconnect callback : disconnects) {
            callback.onConfigurationDisconnect(handler, client);
        }
    });

    @FunctionalInterface
    public interface Init {
        void onConfigurationInit(ClientConfigurationNetworkHandler handler, MinecraftClient client);
    }

    @FunctionalInterface
    public interface Start {
        void onConfigurationStart(ClientConfigurationNetworkHandler handler, MinecraftClient client);
    }

    @FunctionalInterface
    public interface Complete {
        void onConfigurationComplete(ClientConfigurationNetworkHandler handler, MinecraftClient client);
    }

    @FunctionalInterface
    public interface Disconnect {
        void onConfigurationDisconnect(ClientConfigurationNetworkHandler handler, MinecraftClient client);
    }
}
