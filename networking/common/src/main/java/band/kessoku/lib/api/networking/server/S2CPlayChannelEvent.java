package band.kessoku.lib.api.networking.server;

import band.kessoku.lib.api.networking.PacketSender;
import band.kessoku.lib.event.api.Event;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;

import java.util.List;

/**
 * Offers access to events related to the indication of a connected client's ability to receive packets in certain channels.
 */
public final class S2CPlayChannelEvent {
    /**
     * An event for the server play network handler receiving an update indicating the connected client's ability to receive packets in certain channels.
     * This event may be invoked at any time after login and up to disconnection.
     */
    public static final Event<Register> REGISTER = Event.of(registers -> (handler, sender, server, channels) -> {
        for (Register callback : registers) {
            callback.onChannelRegister(handler, sender, server, channels);
        }
    });

    /**
     * An event for the server play network handler receiving an update indicating the connected client's lack of ability to receive packets in certain channels.
     * This event may be invoked at any time after login and up to disconnection.
     */
    public static final Event<Unregister> UNREGISTER = Event.of(unregisters -> (handler, sender, server, channels) -> {
        for (Unregister callback : unregisters) {
            callback.onChannelUnregister(handler, sender, server, channels);
        }
    });

    /**
     * @see S2CPlayChannelEvent#REGISTER
     */
    @FunctionalInterface
    public interface Register {
        void onChannelRegister(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server, List<Identifier> channels);
    }

    /**
     * @see S2CPlayChannelEvent#UNREGISTER
     */
    @FunctionalInterface
    public interface Unregister {
        void onChannelUnregister(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server, List<Identifier> channels);
    }
}