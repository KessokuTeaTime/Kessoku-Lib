package band.kessoku.lib.api.networking.server;

import java.util.List;

import band.kessoku.lib.api.networking.PacketSender;
import band.kessoku.lib.event.api.Event;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerConfigurationNetworkHandler;
import net.minecraft.util.Identifier;

/**
 * Offers access to events related to the indication of a connected client's ability to receive packets in certain channels.
 */
public final class S2CConfigurationChannelEvent {
    /**
     * An event for the server configuration network handler receiving an update indicating the connected client's ability to receive packets in certain channels.
     * This event may be invoked at any time after login and up to disconnection.
     */
    public static final Event<Register> REGISTER = Event.of(registers -> (handler, sender, server, channels) -> {
        for (Register callback : registers) {
            callback.onChannelRegister(handler, sender, server, channels);
        }
    });

    /**
     * An event for the server configuration network handler receiving an update indicating the connected client's lack of ability to receive packets in certain channels.
     * This event may be invoked at any time after login and up to disconnection.
     */
    public static final Event<Unregister> UNREGISTER = Event.of(unregisters -> (handler, sender, server, channels) -> {
        for (Unregister callback : unregisters) {
            callback.onChannelUnregister(handler, sender, server, channels);
        }
    });

    /**
     * @see S2CConfigurationChannelEvent#REGISTER
     */
    @FunctionalInterface
    public interface Register {
        void onChannelRegister(ServerConfigurationNetworkHandler handler, PacketSender sender, MinecraftServer server, List<Identifier> channels);
    }

    /**
     * @see S2CConfigurationChannelEvent#UNREGISTER
     */
    @FunctionalInterface
    public interface Unregister {
        void onChannelUnregister(ServerConfigurationNetworkHandler handler, PacketSender sender, MinecraftServer server, List<Identifier> channels);
    }
}
