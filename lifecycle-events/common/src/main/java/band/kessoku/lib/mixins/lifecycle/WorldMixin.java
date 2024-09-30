/*
 * Copyright (c) 2024 KessokuTeaTime
 *
 * Licensed under the GNU Lesser General Pubic License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package band.kessoku.lib.mixins.lifecycle;

import java.util.HashSet;
import java.util.Set;

import band.kessoku.lib.impl.event.lifecycle.LoadedChunksCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

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
