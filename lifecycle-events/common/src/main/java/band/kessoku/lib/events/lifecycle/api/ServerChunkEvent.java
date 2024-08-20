package band.kessoku.lib.events.lifecycle.api;

import band.kessoku.lib.event.api.Event;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.WorldChunk;

public class ServerChunkEvent {

    /**
     * Called when a chunk is loaded into a ServerWorld.
     *
     * <p>When this event is called, the chunk is already in the world.
     */
    public static final Event<Loaded> LOADED = Event.of(loadeds -> (serverWorld, chunk) -> {
        for (Loaded loaded : loadeds) {
            loaded.onChunkLoaded(serverWorld, chunk);
        }
    });

    /**
     * Called when a chunk is unloaded from a ServerWorld.
     *
     * <p>When this event is called, the chunk is still present in the world.
     */
    public static final Event<Unloaded> UNLOADED = Event.of(unloadeds -> (serverWorld, chunk) -> {
        for (Unloaded unloaded : unloadeds) {
            unloaded.onChunkUnloaded(serverWorld, chunk);
        }
    });

    @FunctionalInterface
    public interface Loaded {
        void onChunkLoaded(ServerWorld world, WorldChunk chunk);
    }

    @FunctionalInterface
    public interface Unloaded {
        void onChunkUnloaded(ServerWorld world, WorldChunk chunk);
    }
}
