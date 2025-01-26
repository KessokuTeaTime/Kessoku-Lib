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
package band.kessoku.lib.api.event.lifecycle.client;

import band.kessoku.lib.event.api.Event;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

public class ClientEntityEvent {

    /**
     * Called when an Entity is loaded into a ClientWorld.
     *
     * <p>When this event is called, the chunk is already in the world.
     */
    public static final Event<Loaded> LOADED = Event.of(loadeds -> (entity, world) -> {
        for (Loaded loaded : loadeds) {
            loaded.onLoaded(entity, world);
        }
    });

    /**
     * Called when an Entity is about to be unloaded from a ClientWorld.
     *
     * <p>This event is called before the entity is unloaded from the world.
     */
    public static final Event<Unloaded> UNLOADED = Event.of(unloadeds -> (entity, world) -> {
        for (Unloaded unloaded : unloadeds) {
            unloaded.onUnloaded(entity, world);
        }
    });

    @FunctionalInterface
    public interface Loaded {
        void onLoaded(Entity entity, ClientWorld world);
    }

    @FunctionalInterface
    public interface Unloaded {
        void onUnloaded(Entity entity, ClientWorld world);
    }
}
