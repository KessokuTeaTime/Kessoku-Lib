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
package band.kessoku.lib.api.event.entity;

import band.kessoku.lib.event.api.Event;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;

/**
 * Events related to entities in combat.
 */
@ApiStatus.NonExtendable
public interface ServerEntityCombatEvent {
    /**
     * An event that is called after an entity is directly responsible for killing another entity.
     *
     * @see Entity#onKilledOther(ServerWorld, LivingEntity)
     */
    Event<AfterKilledOtherEntity> AFTER_KILLED_OTHER_ENTITY = Event.of(callbacks -> (world, entity, killedEntity) -> {
        for (AfterKilledOtherEntity callback : callbacks) {
            callback.afterKilledOtherEntity(world, entity, killedEntity);
        }
    });

    @FunctionalInterface
    interface AfterKilledOtherEntity {
        /**
         * Called after an entity has killed another entity.
         *
         * @param world the world
         * @param entity the entity
         * @param killedEntity the entity which was killed by the {@code entity}
         */
        void afterKilledOtherEntity(ServerWorld world, Entity entity, LivingEntity killedEntity);
    }
}
