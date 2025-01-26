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

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;

public class ServerEntityEvent {
    /**
     * Called when an Entity is loaded into a ServerWorld.
     *
     * <p>When this event is called, the entity is already in the world.
     */
    public static final Event<Loaded> LOADED = Event.of(loadeds -> (entity, world) -> {
        for (Loaded loaded : loadeds) {
            loaded.onLoaded(entity, world);
        }
    });

    /**
     * Called when an Entity is unloaded from a ServerWorld.
     *
     * <p>This event is called before the entity is removed from the world.
     */
    public static final Event<Unloaded> UNLOADED = Event.of(unloadeds -> (entity, world) -> {
        for (Unloaded unloaded : unloadeds) {
            unloaded.onUnloaded(entity, world);
        }
    });

    /**
     * Called during {@link LivingEntity#tick()} if the Entity's equipment has been changed or mutated.
     *
     * <p>This event is also called when the entity joins the world.
     * A change in equipment is determined by {@link ItemStack#areEqual(ItemStack, ItemStack)}.
     */
    public static final Event<EquipmentChanged> EQUIPMENT_CHANGED = Event.of(equipmentChangeds -> (livingEntity, equipmentSlot, previous, next) -> {
        for (EquipmentChanged equipmentChanged : equipmentChangeds) {
            equipmentChanged.onChanged(livingEntity, equipmentSlot, previous, next);
        }
    });

    @FunctionalInterface
    public interface Loaded {
        void onLoaded(Entity entity, ServerWorld world);
    }

    @FunctionalInterface
    public interface Unloaded {
        void onUnloaded(Entity entity, ServerWorld world);
    }

    @FunctionalInterface
    public interface EquipmentChanged {
        void onChanged(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack previousStack, ItemStack currentStack);
    }
}
