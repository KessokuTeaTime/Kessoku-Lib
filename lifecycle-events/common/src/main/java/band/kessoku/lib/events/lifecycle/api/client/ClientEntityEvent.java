package band.kessoku.lib.events.lifecycle.api.client;

import band.kessoku.lib.event.api.Event;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

public class ClientEntityEvent {

    /**
     * Called when an Entity is loaded into a ClientWorld.
     *
     * <p>When this event is called, the chunk is already in the world.
     */
    public static final Event<Loaded> LOADED = Event.of(loadeds -> (entity, world) -> {
        for (Loaded loaded : loadeds) {
            loaded.onLoaded(entity, world);
        }
    });

    /**
     * Called when an Entity is about to be unloaded from a ClientWorld.
     *
     * <p>This event is called before the entity is unloaded from the world.
     */
    public static final Event<Unloaded> UNLOADED = Event.of(unloadeds -> (entity, world) -> {
        for (Unloaded unloaded : unloadeds) {
            unloaded.onUnloaded(entity, world);
        }
    });

    @FunctionalInterface
    public interface Loaded {
        void onLoaded(Entity entity, ClientWorld world);
    }

    @FunctionalInterface
    public interface Unloaded {
        void onUnloaded(Entity entity, ClientWorld world);
    }
}
