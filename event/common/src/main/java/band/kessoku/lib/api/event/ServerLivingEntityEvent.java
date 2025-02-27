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

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;

/**
 * Various server-side only events related to living entities.
 */
@ApiStatus.NonExtendable
public interface ServerLivingEntityEvent {
    /**
     * An event that is called when a living entity is going to take damage.
     * This is fired from {@link LivingEntity#damage}, before armor or any other mitigation are applied.
     * Mods can cancel this to prevent the damage entirely.
     */
    Event<AllowDamage> ALLOW_DAMAGE = Event.of(callbacks -> (entity, source, amount) -> {
        for (AllowDamage callback : callbacks) {
            if (!callback.allowDamage(entity, source, amount)) {
                return false;
            }
        }

        return true;
    });

    /**
     * An event that is called after an entity is damaged. This is fired from {@link LivingEntity#damage} after damage
     * is applied, or after that damage was blocked by a shield.
     *
     * <p>The base damage taken is the damage initially applied to the entity. Damage taken is the amount of damage the
     * entity actually took, after effects such as shields and extra freezing damage are applied. Damage taken does NOT
     * include damage reduction from armor and enchantments.
     *
     * <p>This event is not fired if the entity was killed by the damage.
     */
    Event<AfterDamage> AFTER_DAMAGE = Event.of(callbacks -> (entity, source, baseDamageTaken, damageTaken, blocked) -> {
        for (AfterDamage callback : callbacks) {
            callback.afterDamage(entity, source, baseDamageTaken, damageTaken, blocked);
        }
    });

    /**
     * An event that is called when an entity takes fatal damage.
     *
     * <p>Mods can cancel this to keep the entity alive.
     *
     * <p>Vanilla checks for entity health {@code <= 0} each tick (with {@link LivingEntity#isDead()}), and kills if true -
     * so the entity will still die next tick if this event is cancelled.
     * It's assumed that the listener will do something to prevent this, for example, if the entity is a player:
     * <ul>
     *     <li>a minigame mod teleporting the player into a 'respawn room' and setting their health to 20.0</li>
     *     <li>a mod that changes death mechanics switching the player over to the mod's play-mode, where death doesn't apply</li>
     * </ul>
     */
    Event<AllowDeath> ALLOW_DEATH = Event.of(callbacks -> (entity, damageSource, damageAmount) -> {
        for (AllowDeath callback : callbacks) {
            if (!callback.allowDeath(entity, damageSource, damageAmount)) {
                return false;
            }
        }

        return true;
    });

    /**
     * An event that is called when a living entity dies.
     */
    Event<AfterDeath> AFTER_DEATH = Event.of(callbacks -> (entity, damageSource) -> {
        for (AfterDeath callback : callbacks) {
            callback.afterDeath(entity, damageSource);
        }
    });

    /**
     * An event that is called when a mob has been converted to another type.
     *
     * <p>When this event is called, the old instance has not yet been discarded, and the new one has not yet been spawned.
     * Mods may use this event to copy some of the old entity's data to the converted one.</p>
     *
     * <p>This event only handles cases where the entity type changes, requiring a new instance. Notably it does not
     * cover mooshrooms changing color from lightning, creepers getting charged, or wolves being tamed.</p>
     */
    Event<MobConversion> MOB_CONVERSION = Event.of(callbacks -> (previous, converted, keepEquipment) -> {
        for (MobConversion callback : callbacks) {
            callback.onConversion(previous, converted, keepEquipment);
        }
    });

    @FunctionalInterface
    interface AllowDamage {
        /**
         * Called when a living entity is going to take damage. Can be used to cancel the damage entirely.
         *
         * <p>The amount corresponds to the "incoming" damage amount, before armor and other mitigations have been applied.
         *
         * @param entity the entity
         * @param source the source of the damage
         * @param amount the amount of damage that the entity will take (before mitigations)
         * @return true if the damage should go ahead, false to cancel the damage.
         */
        boolean allowDamage(LivingEntity entity, DamageSource source, float amount);
    }

    @FunctionalInterface
    interface AfterDamage {
        /**
         * Called after a living entity took damage, unless they were killed. The base damage taken is given as damage
         * taken before armor or enchantments are applied, but after other effects like shields are applied.
         *
         * @param entity          the entity that was damaged
         * @param source          the source of the damage
         * @param baseDamageTaken the amount of damage initially dealt
         * @param damageTaken     the amount of damage actually taken by the entity, before armor and enchantment effects
         * @param blocked         whether the damage was blocked by a shield
         */
        void afterDamage(LivingEntity entity, DamageSource source, float baseDamageTaken, float damageTaken, boolean blocked);
    }

    @FunctionalInterface
    interface AllowDeath {
        /**
         * Called when a living entity takes fatal damage (before totems of undying can take effect).
         *
         * @param entity       the entity
         * @param damageSource the source of the fatal damage
         * @param damageAmount the amount of damage that has killed the entity
         * @return true if the death should go ahead, false to cancel the death.
         */
        boolean allowDeath(LivingEntity entity, DamageSource damageSource, float damageAmount);
    }

    @FunctionalInterface
    interface AfterDeath {
        /**
         * Called when a living entity dies. The death cannot be canceled at this point.
         *
         * @param entity       the entity
         * @param damageSource the source of the fatal damage
         */
        void afterDeath(LivingEntity entity, DamageSource damageSource);
    }

    @FunctionalInterface
    interface MobConversion {
        /**
         * Called when a mob is converted to another type.
         *
         * @param previous      the previous entity instance
         * @param converted     the new instance for the converted entity
         * @param keepEquipment whether the converted entity should keep the previous one's equipment, like armor
         */
        void onConversion(MobEntity previous, MobEntity converted, boolean keepEquipment);
    }
}
