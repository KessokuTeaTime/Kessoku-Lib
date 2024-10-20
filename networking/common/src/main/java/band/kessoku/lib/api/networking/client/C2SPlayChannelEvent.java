package band.kessoku.lib.api.networking.client;

import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.util.Identifier;

import band.kessoku.lib.event.api.Event;
import band.kessoku.lib.api.networking.PacketSender;

/**
 * Offers access to events related to the indication of a connected server's ability to receive packets in certain channels.
 */
public final class C2SPlayChannelEvent {
    /**
     * An event for the client play network handler receiving an update indicating the connected server's ability to receive packets in certain channels.
     * This event may be invoked at any time after login and up to disconnection.
     */
    public static final Event<Register> REGISTER = Event.of(registers -> (handler, sender, client, channels) -> {
        for (Register callback : registers) {
            callback.onChannelRegister(handler, sender, client, channels);
        }
    });

    /**
     * An event for the client play network handler receiving an update indicating the connected server's lack of ability to receive packets in certain channels.
     * This event may be invoked at any time after login and up to disconnection.
     */
    public static final Event<Unregister> UNREGISTER = Event.of(unregisters -> (handler, sender, client, channels) -> {
        for (Unregister callback : unregisters) {
            callback.onChannelUnregister(handler, sender, client, channels);
        }
    });

    /**
     * @see C2SPlayChannelEvent#REGISTER
     */
    @FunctionalInterface
    public interface Register {
        void onChannelRegister(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client, List<Identifier> channels);
    }

    /**
     * @see C2SPlayChannelEvent#UNREGISTER
     */
    @FunctionalInterface
    public interface Unregister {
        void onChannelUnregister(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client, List<Identifier> channels);
    }
}
