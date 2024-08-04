package band.kessoku.lib.events.lifecycle.impl;

import band.kessoku.lib.events.lifecycle.api.ServerBlockEntityEvent;
import band.kessoku.lib.events.lifecycle.api.ServerChunkEvent;
import band.kessoku.lib.events.lifecycle.api.ServerEntityEvent;
import band.kessoku.lib.events.lifecycle.api.ServerWorldEvent;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.chunk.WorldChunk;

public class KessokuLifecycleEventsImpl {
    public static void clientInit() {

    }

    public static void init() {
        // Part of impl for block entity events
        ServerChunkEvent.LOADED.register((world, chunk) -> {
            ((LoadedChunksCache) world).kessoku$markLoaded(chunk);
        });

        ServerChunkEvent.UNLOADED.register((world, chunk) -> {
            ((LoadedChunksCache) world).kessoku$markUnloaded(chunk);
        });

        // Fire block entity unload events.
        // This handles the edge case where going through a portal will cause block entities to unload without warning.
        ServerChunkEvent.UNLOADED.register((world, chunk) -> {
            for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                ServerBlockEntityEvent.UNLOADED.invoker().onUnloaded(blockEntity, world);
            }
        });

        // We use the world unload event so worlds that are dynamically hot(un)loaded get (block) entity unload events fired when shut down.
        ServerWorldEvent.UNLOADED.register((server, world) -> {
            for (WorldChunk chunk : ((LoadedChunksCache) world).kessoku$getLoadedChunks()) {
                for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                    ServerBlockEntityEvent.UNLOADED.invoker().onUnloaded(blockEntity, world);
                }
            }

            for (Entity entity : world.iterateEntities()) {
                ServerEntityEvent.UNLOADED.invoker().onUnloaded(entity, world);
            }
        });
    }
}
