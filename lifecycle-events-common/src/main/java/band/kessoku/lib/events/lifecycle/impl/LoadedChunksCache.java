package band.kessoku.lib.events.lifecycle.impl;

import net.minecraft.world.chunk.WorldChunk;

import java.util.Set;

/**
 * A simple marker interface which holds references to chunks which block entities may be loaded or unloaded from.
 */
public interface LoadedChunksCache {
    Set<WorldChunk> kessoku$getLoadedChunks();

    /**
     * Marks a chunk as loaded in a world.
     */
    void kessoku$markLoaded(WorldChunk chunk);

    /**
     * Marks a chunk as unloaded in a world.
     */
    void kessoku$markUnloaded(WorldChunk chunk);
}
