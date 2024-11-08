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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;

public class ClientTickEvent {

    /**
     * Called at the start of the client tick.
     */
    public static final Event<ClientTick.Start> START_CLIENT_TICK = Event.of(starts -> client -> {
        for (ClientTick.Start start : starts) {
            start.onStartTick(client);
        }
    });

    /**
     * Called at the end of the client tick.
     */
    public static final Event<ClientTick.End> END_CLIENT_TICK = Event.of(ends -> client -> {
        for (ClientTick.End end : ends) {
            end.onEndTick(client);
        }
    });

    /**
     * Called at the start of a ClientWorld's tick.
     */
    public static final Event<WorldTick.Start> START_WORLD_TICK = Event.of(starts -> world -> {
        for (WorldTick.Start start : starts) {
            start.onStartTick(world);
        }
    });

    /**
     * Called at the end of a ClientWorld's tick.
     *
     * <p>End of world tick may be used to start async computations for the next tick.
     */
    public static final Event<WorldTick.End> END_WORLD_TICK = Event.of(ends -> world -> {
        for (WorldTick.End end : ends) {
            end.onEndTick(world);
        }
    });

    public interface ClientTick {
        @FunctionalInterface
        interface Start {
            void onStartTick(MinecraftClient client);
        }

        @FunctionalInterface
        interface End {
            void onEndTick(MinecraftClient client);
        }
    }

    public interface WorldTick {
        @FunctionalInterface
        interface Start {
            void onStartTick(ClientWorld world);
        }

        @FunctionalInterface
        interface End {
            void onEndTick(ClientWorld world);
        }
    }
}
