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
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;

public class ServerEntityEvents {
    private ServerEntityEvents() {
    }

    /**
     * Called when an Entity is loaded into a ServerWorld.
     *
     * <p>When this event is called, the entity is already in the world.
     */
    public static final Event<Load> LOAD = Event.of(callbacks -> (entity, world) -> {
        for (Load callback : callbacks) {
            callback.onLoad(entity, world);
        }
    });

    /**
     * Called when an Entity is unloaded from a ServerWorld.
     *
     * <p>This event is called before the entity is removed from the world.
     */
    public static final Event<Unload> UNLOAD = Event.of(callbacks -> (entity, world) -> {
        for (Unload callback : callbacks) {
            callback.onUnload(entity, world);
        }
    });

    /**
     * Called during {@link LivingEntity#tick()} if the Entity's equipment has been changed or mutated.
     *
     * <p>This event is also called when the entity joins the world.
     * A change in equipment is determined by {@link ItemStack#areEqual(ItemStack, ItemStack)}.
     */
    public static final Event<EquipmentChange> EQUIPMENT_CHANGE = Event.of(callbacks -> (livingEntity, equipmentSlot, previous, next) -> {
        for (EquipmentChange callback : callbacks) {
            callback.onChange(livingEntity, equipmentSlot, previous, next);
        }
    });

    @FunctionalInterface
    public interface Load {
        void onLoad(Entity entity, ServerWorld world);
    }

    @FunctionalInterface
    public interface Unload {
        void onUnload(Entity entity, ServerWorld world);
    }

    @FunctionalInterface
    public interface EquipmentChange {
        void onChange(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack previousStack, ItemStack currentStack);
    }
}
