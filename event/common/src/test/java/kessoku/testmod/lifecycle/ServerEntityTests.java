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
package kessoku.testmod.lifecycle;

import java.util.ArrayList;
import java.util.List;

import band.kessoku.lib.api.entrypoint.entrypoints.KessokuModInitializer;
import band.kessoku.lib.api.event.ServerEntityEvents;

import net.minecraft.entity.Entity;

public class ServerEntityTests implements KessokuModInitializer {
    private final List<Entity> serverEntities = new ArrayList<>();

    @Override
    public void onInitialize() {
        ServerEntityEvents.LOADED.register(((entity, world) -> {
            this.serverEntities.add(entity);

            KessokuTestLifecycle.LOGGER.info("[SERVER] LOADED {} - Entities: {}", entity.toString(), this.serverEntities.size());
        }));

        ServerEntityEvents.UNLOADED.register(((entity, world) -> {
            this.serverEntities.remove(entity);

            KessokuTestLifecycle.LOGGER.info("[SERVER] UNLOADED {} - Entities: {}", entity.toString(), this.serverEntities.size());
        }));

        ServerEntityEvents.EQUIPMENT_CHANGED.register(((livingEntity, equipmentSlot, previous, next) -> {
            KessokuTestLifecycle.LOGGER.info("[SERVER] Entity equipment change: Entity: {}, Slot {}, Previous: {}, Current {} ", livingEntity, equipmentSlot.name(), previous, next);
        }));
    }
}
