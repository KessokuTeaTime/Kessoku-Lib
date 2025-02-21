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
package band.kessoku.lib.api.client.event;

import band.kessoku.lib.api.event.Event;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.chunk.WorldChunk;

@ApiStatus.NonExtendable
public interface ClientChunkEvent {
    /**
     * Called when a chunk is loaded into a ClientWorld.
     *
     * <p>When this event is called, the chunk is already in the world.
     */
    Event<Loaded> LOADED = Event.of(loadeds -> (clientWorld, chunk) -> {
        for (Loaded callback : loadeds) {
            callback.onChunkLoaded(clientWorld, chunk);
        }
    });

    /**
     * Called when a chunk is about to be unloaded from a ClientWorld.
     *
     * <p>When this event is called, the chunk is still present in the world.
     */
    Event<Unloaded> UNLOADED = Event.of(unloadeds -> (clientWorld, chunk) -> {
        for (Unloaded unloaded : unloadeds) {
            unloaded.onChunkUnloaded(clientWorld, chunk);
        }
    });

    @FunctionalInterface
    interface Loaded {
        void onChunkLoaded(ClientWorld world, WorldChunk chunk);
    }

    @FunctionalInterface
    interface Unloaded {
        void onChunkUnloaded(ClientWorld world, WorldChunk chunk);
    }
}
