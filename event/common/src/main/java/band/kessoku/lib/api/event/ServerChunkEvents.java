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
package band.kessoku.lib.api.event;


import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.WorldChunk;

public class ServerChunkEvents {

    private ServerChunkEvents() {
    }

    /**
     * Called when a chunk is loaded into a ServerWorld.
     *
     * <p>When this event is called, the chunk is already in the world.
     */
    public static final Event<Loaded> LOADED = Event.of(callbacks -> (serverWorld, chunk) -> {
        for (Loaded callback : callbacks) {
            callback.onChunkLoaded(serverWorld, chunk);
        }
    });

    /**
     * Called when a newly generated chunk is loaded into a ServerWorld.
     *
     * <p>When this event is called, the chunk is already in the world.
     */
    public static final Event<Generate> GENERATE = Event.of(callbacks -> (serverWorld, chunk) -> {
        for (Generate callback : callbacks) {
            callback.onChunkGenerate(serverWorld, chunk);
        }
    });

    /**
     * Called when a chunk is unloaded from a ServerWorld.
     *
     * <p>When this event is called, the chunk is still present in the world.
     */
    public static final Event<Unloaded> UNLOADED = Event.of(callbacks -> (serverWorld, chunk) -> {
        for (Unloaded callback : callbacks) {
            callback.onChunkUnloaded(serverWorld, chunk);
        }
    });

    @FunctionalInterface
    public interface Loaded {
        void onChunkLoaded(ServerWorld world, WorldChunk chunk);
    }

    @FunctionalInterface
    public interface Generate {
        void onChunkGenerate(ServerWorld world, WorldChunk chunk);
    }

    @FunctionalInterface
    public interface Unloaded {
        void onChunkUnloaded(ServerWorld world, WorldChunk chunk);
    }
}
