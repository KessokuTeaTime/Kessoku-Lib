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
package kessoku.testmod.lifecycle;

import band.kessoku.lib.entrypoint.api.KessokuModInitializer;
import band.kessoku.lib.events.lifecycle.api.ServerTickEvent;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class ServerTickTests implements KessokuModInitializer {
    private final Map<RegistryKey<World>, Integer> tickTracker = new HashMap<>();

    @Override
    public void onInitialize() {
        ServerTickEvent.START_WORLD_TICK.register(world -> {
            // Verify we are inside the tick
            if (!world.isInBlockTick()) {
                throw new AssertionError("Start tick event should be fired while ServerWorld is inside of block tick");
            }
        });

        ServerTickEvent.END_WORLD_TICK.register(world -> {
            final int worldTicks = tickTracker.computeIfAbsent(world.getRegistryKey(), k -> 0);

            if (worldTicks % 200 == 0) { // Log every 200 ticks to verify the tick callback works on the server world
                KessokuTestLifecycle.LOGGER.info("Ticked Server World - {} ticks:{}", worldTicks, world.getRegistryKey().getValue());
            }

            this.tickTracker.put(world.getRegistryKey(), worldTicks + 1);
        });

        ServerTickEvent.START_SERVER_TICK.register(server -> {
            KessokuTestLifecycle.LOGGER.info("Server tick started");
        });

        ServerTickEvent.END_SERVER_TICK.register(server -> {
            if (server.getTicks() % 200 == 0) { // Log every 200 ticks to verify the tick callback works on the server
                KessokuTestLifecycle.LOGGER.info("Ticked Server at {} ticks.", server.getTicks());
            }
        });
    }
}
