package band.kessoku.lib.events.lifecycle.api.client;

import band.kessoku.lib.event.api.Event;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.chunk.WorldChunk;

public class ClientChunkEvent {

    /**
     * Called when a chunk is loaded into a ClientWorld.
     *
     * <p>When this event is called, the chunk is already in the world.
     */
    public static final Event<Loaded> LOADED = Event.of(loadeds -> (clientWorld, chunk) -> {
        for (Loaded callback : loadeds) {
            callback.onChunkLoaded(clientWorld, chunk);
        }
    });

    /**
     * Called when a chunk is about to be unloaded from a ClientWorld.
     *
     * <p>When this event is called, the chunk is still present in the world.
     */
    public static final Event<Unloaded> UNLOADED = Event.of(unloadeds -> (clientWorld, chunk) -> {
        for (Unloaded unloaded : unloadeds) {
            unloaded.onChunkUnloaded(clientWorld, chunk);
        }
    });

    @FunctionalInterface
    public interface Loaded {
        void onChunkLoaded(ClientWorld world, WorldChunk chunk);
    }

    @FunctionalInterface
    public interface Unloaded {
        void onChunkUnloaded(ClientWorld world, WorldChunk chunk);
    }
}
