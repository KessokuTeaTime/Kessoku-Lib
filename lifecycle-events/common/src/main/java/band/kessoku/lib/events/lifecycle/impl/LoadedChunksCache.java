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
package band.kessoku.lib.events.lifecycle.impl;

import java.util.Set;

import net.minecraft.world.chunk.WorldChunk;

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
