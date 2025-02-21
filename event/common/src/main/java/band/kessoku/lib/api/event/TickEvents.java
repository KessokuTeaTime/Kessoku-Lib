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


import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class TickEvents {
    /**
     * Called at the start of the server tick.
     *
     * <p>When the dedicated server is "paused", this event is not invoked.
     */
    public static final Event<StartServerTick> START_SERVER_TICK = Event.of(callbacks -> server -> {
        for (StartServerTick event : callbacks) {
            event.onStartTick(server);
        }
    });

    /**
     * Called at the end of the server tick.
     *
     * <p>When the dedicated server is "paused", this event is not invoked.
     */
    public static final Event<EndServerTick> END_SERVER_TICK = Event.of(callbacks -> server -> {
        for (EndServerTick event : callbacks) {
            event.onEndTick(server);
        }
    });

    /**
     * Called at the start of a World's tick.
     *
     * <p>When the dedicated server is "paused", this event is not invoked.
     */
    public static final Event<StartWorldTick> START_WORLD_TICK = Event.of(callbacks -> world -> {
        for (StartWorldTick callback : callbacks) {
            callback.onStartTick(world);
        }
    });

    /**
     * Called at the end of a World's tick.
     *
     * <p>End of world tick may be used to start async computations for the next tick.
     *
     * <p>When the dedicated server is "paused", this event is not invoked.
     */
    public static final Event<EndWorldTick> END_WORLD_TICK = Event.of(callbacks -> world -> {
        for (EndWorldTick callback : callbacks) {
            callback.onEndTick(world);
        }
    });

    @FunctionalInterface
    public interface StartServerTick {
        void onStartTick(MinecraftServer server);
    }

    @FunctionalInterface
    public interface EndServerTick {
        void onEndTick(MinecraftServer server);
    }

    @FunctionalInterface
    public interface StartWorldTick {
        void onStartTick(World world);
    }

    @FunctionalInterface
    public interface EndWorldTick {
        void onEndTick(World world);
    }

    @FunctionalInterface
    public interface StartEntityTick {
        boolean onStartTick(Entity entity);
    }

    @FunctionalInterface
    public interface EndEntityTick {
        void onEndTick(Entity entity);
    }

    @FunctionalInterface
    public interface StartPlayerTick {
        void onStartTick(PlayerEntity player);
    }

    @FunctionalInterface
    public interface EndPlayerTick {
        void onEndTick(PlayerEntity player);
    }
}
