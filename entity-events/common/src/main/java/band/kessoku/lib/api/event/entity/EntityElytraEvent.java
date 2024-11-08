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
package band.kessoku.lib.api.event.entity;

import band.kessoku.lib.api.event.entity.item.KessokuElytraItem;
import band.kessoku.lib.event.api.Event;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.ApiStatus;

/**
 * Events related to elytra flight for living entities. Elytra flight is also known as "fall flying".
 */
@ApiStatus.NonExtendable
public interface EntityElytraEvent {

    /**
     * An event to check if elytra flight (both through normal and custom elytras) is allowed.
     * All listeners need to return true to allow the entity to fly, otherwise elytra flight will be blocked/stopped.
     */
    Event<Allow> ALLOW = Event.of(listeners -> entity -> {
        for (Allow listener : listeners) {
            if (!listener.allowElytraFlight(entity)) {
                return false;
            }
        }

        return true;
    });

    /**
     * An event to grant elytra flight to living entities when some condition is met.
     * Will be called when players try to start elytra flight by pressing space in mid-air, and every tick for all flying living entities to check if elytra flight is still allowed.
     *
     * <p>Items that wish to enable custom elytra flight when worn in the chest equipment slot can simply implement {@link KessokuElytraItem} instead of registering a listener.
     */
    Event<Custom> CUSTOM = Event.of(listeners -> (entity, tickElytra) -> {
        for (Custom listener : listeners) {
            if (listener.useCustomElytra(entity, tickElytra)) {
                return true;
            }
        }

        return false;
    });

    @FunctionalInterface
    interface Allow {
        /**
         * @return false to block elytra flight, true to allow it (unless another listener returns false)
         */
        boolean allowElytraFlight(LivingEntity entity);
    }

    @FunctionalInterface
    interface Custom {
        /**
         * Try to use a custom elytra for an entity.
         * A custom elytra is anything that allows an entity to enter and continue elytra flight when some condition is met.
         * Listeners should follow the following pattern:
         * <pre>{@code
         * EntityElytraEvents.CUSTOM.register((entity, tickElytra) -> {
         *     if (check if condition for custom elytra is met) {
         *         if (tickElytra) {
         *             // Optionally consume some resources that are being used up in order to fly, for example damaging an item.
         *             // Optionally perform other side effects of elytra flight, for example playing a sound.
         *         }
         *         // Allow entering/continuing elytra flight with this custom elytra
         *         return true;
         *     }
         *     // Condition for the custom elytra is not met: don't let players enter or continue elytra flight (unless another elytra is available).
         *     return false;
         * });
         * }</pre>
         *
         * @param entity     the entity
         * @param tickElytra false if this is just to check if the custom elytra can be used, true if the custom elytra should also be ticked, i.e. perform side-effects of flying such as using resources.
         * @return true to use a custom elytra, enabling elytra flight for the entity and cancelling subsequent handlers
         */
        boolean useCustomElytra(LivingEntity entity, boolean tickElytra);
    }
}
