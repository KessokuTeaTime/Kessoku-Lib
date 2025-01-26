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
package band.kessoku.lib.api.event.lifecycle;

import band.kessoku.lib.event.api.Event;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

public class ServerWorldEvent {

    /**
     * Called just after a world is loaded by a Minecraft server.
     *
     * <p>This can be used to load world specific metadata or initialize a {@link net.minecraft.world.PersistentState} on a server world.
     */
    public static final Event<Loaded> LOADED = Event.of(loadeds -> (server, world) -> {
        for (Loaded loaded : loadeds) {
            loaded.onWorldLoaded(server, world);
        }
    });

    /**
     * Called before a world is unloaded by a Minecraft server.
     *
     * <p>This typically occurs after a server has {@link ServerLifecycleEvent#STOPPING started shutting down}.
     * Mods which allow dynamic world (un)registration should call this event so mods can let go of world handles when a world is removed.
     */
    public static final Event<Unloaded> UNLOADED = Event.of(unloadeds -> (server, world) -> {
        for (Unloaded unloaded : unloadeds) {
            unloaded.onWorldUnloaded(server, world);
        }
    });

    @FunctionalInterface
    public interface Loaded {
        void onWorldLoaded(MinecraftServer server, ServerWorld world);
    }

    @FunctionalInterface
    public interface Unloaded {
        void onWorldUnloaded(MinecraftServer server, ServerWorld world);
    }
}
