package band.kessoku.lib.events.entity.api;

import band.kessoku.lib.event.api.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;

/**
 * Events related to entities in combat.
 */
public class ServerEntityCombatEvent {
    /**
     * An event that is called after an entity is directly responsible for killing another entity.
     *
     * @see Entity#onKilledOther(ServerWorld, LivingEntity)
     */
    public static final Event<AfterKilledOtherEntity> AFTER_KILLED_OTHER_ENTITY = Event.of(callbacks -> (world, entity, killedEntity) -> {
        for (AfterKilledOtherEntity callback : callbacks) {
            callback.afterKilledOtherEntity(world, entity, killedEntity);
        }
    });

    @FunctionalInterface
    public interface AfterKilledOtherEntity {
        /**
         * Called after an entity has killed another entity.
         *
         * @param world the world
         * @param entity the entity
         * @param killedEntity the entity which was killed by the {@code entity}
         */
        void afterKilledOtherEntity(ServerWorld world, Entity entity, LivingEntity killedEntity);
    }

    private ServerEntityCombatEvent() {
    }
}
