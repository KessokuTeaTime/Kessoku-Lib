package band.kessoku.lib.events.lifecycle.api.server;

import band.kessoku.lib.event.api.Event;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

public class ServerWorldEvents {

    /**
     * Called just after a world is loaded by a Minecraft server.
     *
     * <p>This can be used to load world specific metadata or initialize a {@link net.minecraft.world.PersistentState} on a server world.
     */
    public static final Event<Loaded> LOADED = Event.of(loadeds -> (server, world) -> {
        for (Loaded loaded : loadeds) {
            loaded.onWorldLoaded(server, world);
        }
    });

    /**
     * Called before a world is unloaded by a Minecraft server.
     *
     * <p>This typically occurs after a server has {@link ServerLifecycleEvents#STOPPING started shutting down}.
     * Mods which allow dynamic world (un)registration should call this event so mods can let go of world handles when a world is removed.
     */
    public static final Event<Unloaded> UNLOADED = Event.of(unloadeds -> (server, world) -> {
        for (Unloaded unloaded : unloadeds) {
            unloaded.onWorldUnloaded(server, world);
        }
    });

    @FunctionalInterface
    public interface Loaded {
        void onWorldLoaded(MinecraftServer server, ServerWorld world);
    }

    @FunctionalInterface
    public interface Unloaded {
        void onWorldUnloaded(MinecraftServer server, ServerWorld world);
    }
}
