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
package band.kessoku.lib.events.lifecycle.api.client;

import band.kessoku.lib.event.api.Event;

import net.minecraft.client.MinecraftClient;

public class ClientLifecycleEvent {

    /**
     * Called when Minecraft has started and it's client about to tick for the first time.
     *
     * <p>This occurs while the splash screen is displayed.
     */
    public static final Event<Client.Started> STARTED = Event.of(starteds -> client -> {
        for (Client.Started started : starteds) {
            started.onClientStarted(client);
        }
    });

    /**
     * Called when Minecraft's client begins to stop.
     * This is caused by quitting while in game, or closing the game window.
     *
     * <p>This will be called before the integrated server is stopped if it is running.
     */
    public static final Event<Client.Stopping> STOPPING = Event.of(stoppings -> client -> {
        for (Client.Stopping stopping : stoppings) {
            stopping.onClientStopping(client);
        }
    });

    public interface Client {
        @FunctionalInterface
        interface Started {
            void onClientStarted(MinecraftClient client);
        }

        @FunctionalInterface
        interface Stopping {
            void onClientStopping(MinecraftClient client);
        }
    }
}
