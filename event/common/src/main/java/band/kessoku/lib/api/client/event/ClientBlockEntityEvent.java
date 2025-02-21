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

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.world.ClientWorld;

@ApiStatus.NonExtendable
public interface ClientBlockEntityEvent {
    /**
     * Called when a BlockEntity is loaded into a ClientWorld.
     *
     * <p>When this event is called, the block entity is already in the world.
     * However, its data might not be loaded yet, so don't rely on it.
     */
    Event<Loaded> LOADED = Event.of(loadeds -> (blockEntity, world) -> {
        for (Loaded loaded : loadeds) {
            loaded.onLoaded(blockEntity, world);
        }
    });

    /**
     * Called when a BlockEntity is about to be unloaded from a ClientWorld.
     *
     * <p>When this event is called, the block entity is still present on the world.
     */
    Event<Unloaded> UNLOADED = Event.of(unloadeds -> (blockEntity, world) -> {
        for (Unloaded unloaded : unloadeds) {
            unloaded.onUnloaded(blockEntity, world);
        }
    });

    @FunctionalInterface
    interface Loaded {
        void onLoaded(BlockEntity blockEntity, ClientWorld world);
    }

    @FunctionalInterface
    interface Unloaded {
        void onUnloaded(BlockEntity blockEntity, ClientWorld world);
    }
}
