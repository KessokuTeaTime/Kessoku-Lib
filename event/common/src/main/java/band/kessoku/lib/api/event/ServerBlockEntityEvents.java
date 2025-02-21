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


import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;

public class ServerBlockEntityEvents {
    private ServerBlockEntityEvents() {
    }

    /**
     * Called when an BlockEntity is loaded into a ServerWorld.
     *
     * <p>When this is event is called, the block entity is already in the world.
     * However, its data might not be loaded yet, so don't rely on it.
     */
    public static final Event<Load> LOAD = Event.of(callbacks -> (blockEntity, world) -> {
        for (Load callback : callbacks) {
            callback.onLoad(blockEntity, world);
        }
    });

    /**
     * Called when an BlockEntity is about to be unloaded from a ServerWorld.
     *
     * <p>When this event is called, the block entity is still present on the world.
     */
    public static final Event<Unload> UNLOAD = Event.of(callbacks -> (blockEntity, world) -> {
        for (Unload callback : callbacks) {
            callback.onUnload(blockEntity, world);
        }
    });

    @FunctionalInterface
    public interface Load {
        void onLoad(BlockEntity blockEntity, ServerWorld world);
    }

    @FunctionalInterface
    public interface Unload {
        void onUnload(BlockEntity blockEntity, ServerWorld world);
    }
}
