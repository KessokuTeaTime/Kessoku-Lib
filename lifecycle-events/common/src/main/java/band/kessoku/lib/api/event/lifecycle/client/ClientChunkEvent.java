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
package band.kessoku.lib.api.event.lifecycle.client;

import band.kessoku.lib.event.api.Event;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;

public class ClientChunkEvent {

    /**
     * Called when a {@link Chunk} is loaded into a {@link ClientWorld}.
     *
     * <p>When this event is called, the chunk is already in the world.
     */
    public static final Event<Loaded> LOADED = Event.of(loadeds -> (clientWorld, chunk) -> {
        for (Loaded callback : loadeds) {
            callback.onChunkLoaded(clientWorld, chunk);
        }
    });

    /**
     * Called when a {@link Chunk} is about to be unloaded from a {@link ClientWorld}.
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
