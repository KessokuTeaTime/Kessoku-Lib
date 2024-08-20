package band.kessoku.lib.events.lifecycle.api.client;

import band.kessoku.lib.event.api.Event;

import net.minecraft.client.MinecraftClient;

public class ClientLifecycleEvent {

    /**
     * Called when Minecraft has started and it's client about to tick for the first time.
     *
     * <p>This occurs while the splash screen is displayed.
     */
    public static final Event<Client.Started> STARTED = Event.of(starteds -> client -> {
        for (Client.Started started : starteds) {
            started.onClientStarted(client);
        }
    });

    /**
     * Called when Minecraft's client begins to stop.
     * This is caused by quitting while in game, or closing the game window.
     *
     * <p>This will be called before the integrated server is stopped if it is running.
     */
    public static final Event<Client.Stopping> STOPPING = Event.of(stoppings -> client -> {
        for (Client.Stopping stopping : stoppings) {
            stopping.onClientStopping(client);
        }
    });

    public interface Client {
        @FunctionalInterface
        interface Started {
            void onClientStarted(MinecraftClient client);
        }

        @FunctionalInterface
        interface Stopping {
            void onClientStopping(MinecraftClient client);
        }
    }
}
