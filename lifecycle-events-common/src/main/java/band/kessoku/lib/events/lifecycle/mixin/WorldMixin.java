package band.kessoku.lib.events.lifecycle.mixin;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import band.kessoku.lib.events.lifecycle.impl.LoadedChunksCache;

@Mixin(World.class)
public abstract class WorldMixin implements LoadedChunksCache {
    @Unique
    private final Set<WorldChunk> kessoku$loadedChunks = new HashSet<>();

    @Override
    public Set<WorldChunk> kessoku$getLoadedChunks() {
        return this.kessoku$loadedChunks;
    }

    @Override
    public void kessoku$markLoaded(WorldChunk chunk) {
        this.kessoku$loadedChunks.add(chunk);
    }

    @Override
    public void kessoku$markUnloaded(WorldChunk chunk) {
        this.kessoku$loadedChunks.remove(chunk);
    }
}
