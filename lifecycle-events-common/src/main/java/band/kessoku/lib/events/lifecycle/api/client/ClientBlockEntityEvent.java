package band.kessoku.lib.events.lifecycle.api.client;

import band.kessoku.lib.event.api.Event;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.world.ClientWorld;

public class ClientBlockEntityEvent {

    /**
     * Called when a BlockEntity is loaded into a ClientWorld.
     *
     * <p>When this event is called, the block entity is already in the world.
     * However, its data might not be loaded yet, so don't rely on it.
     */
    public static final Event<Loaded> LOADED = Event.of(loadeds -> (blockEntity, world) -> {
        for (Loaded loaded : loadeds) {
            loaded.onLoaded(blockEntity, world);
        }
    });

    /**
     * Called when a BlockEntity is about to be unloaded from a ClientWorld.
     *
     * <p>When this event is called, the block entity is still present on the world.
     */
    public static final Event<Unloaded> UNLOADED = Event.of(unloadeds -> (blockEntity, world) -> {
        for (Unloaded unloaded : unloadeds) {
            unloaded.onUnloaded(blockEntity, world);
        }
    });

    @FunctionalInterface
    public interface Loaded {
        void onLoaded(BlockEntity blockEntity, ClientWorld world);
    }

    @FunctionalInterface
    public interface Unloaded {
        void onUnloaded(BlockEntity blockEntity, ClientWorld world);
    }
}
