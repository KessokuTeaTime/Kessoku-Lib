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

import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

public class WorldEvents {

    private WorldEvents() {
    }

    /**
     * Called just after a world is loaded by a Minecraft server.
     *
     * <p>This can be used to load world specific metadata or initialize a {@link PersistentState} on a server world.
     */
    public static final Event<Load> LOAD = Event.of(callbacks -> (world) -> {
        for (Load callback : callbacks) {
            callback.onWorldLoad(world);
        }
    });

    /**
     * Called before a world is unloaded by a Minecraft server.
     *
     * <p>This typically occurs after a server has {@link ServerLifecycleEvents#STOPPING started shutting down}.
     * Mods which allow dynamic world (un)registration should call this event so mods can let go of world handles when a world is removed.
     */
    public static final Event<Unload> UNLOAD = Event.of(callbacks -> (world) -> {
        for (Unload callback : callbacks) {
            callback.onWorldUnload(world);
        }
    });

    @FunctionalInterface
    public interface Load {
        void onWorldLoad(World world);
    }

    @FunctionalInterface
    public interface Unload {
        void onWorldUnload(World world);
    }
}
