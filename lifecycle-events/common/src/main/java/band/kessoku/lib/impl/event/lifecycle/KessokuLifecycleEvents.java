/*
 * Copyright (c) 2024, 2025 KessokuTeaTime
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
package band.kessoku.lib.impl.event.lifecycle;

import band.kessoku.lib.api.event.lifecycle.ServerBlockEntityEvent;
import band.kessoku.lib.api.event.lifecycle.ServerChunkEvent;
import band.kessoku.lib.api.event.lifecycle.ServerEntityEvent;
import band.kessoku.lib.api.event.lifecycle.ServerWorldEvent;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.chunk.WorldChunk;

public class KessokuLifecycleEvents {
    public static final String MOD_ID = "kessoku_lifecycle_events";
    public static final String NAME = "Kessoku Lifecycle Events API";
    public static final Marker MARKER = MarkerFactory.getMarker("[" + NAME + "]");

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
