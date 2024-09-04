package kessoku.testmod.lifecycle;

import band.kessoku.lib.entrypoint.api.KessokuModInitializer;
import band.kessoku.lib.events.lifecycle.api.ServerEntityEvent;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class ServerEntityTests implements KessokuModInitializer {
    private final List<Entity> serverEntities = new ArrayList<>();

    @Override
    public void onInitialize() {
        ServerEntityEvent.LOADED.register(((entity, world) -> {
            this.serverEntities.add(entity);

            KessokuTestLifecycle.LOGGER.info("[SERVER] LOADED {} - Entities: {}", entity.toString(), this.serverEntities.size());
        }));

        ServerEntityEvent.UNLOADED.register(((entity, world) -> {
            this.serverEntities.remove(entity);

            KessokuTestLifecycle.LOGGER.info("[SERVER] UNLOADED {} - Entities: {}", entity.toString(), this.serverEntities.size());
        }));

        ServerEntityEvent.EQUIPMENT_CHANGED.register(((livingEntity, equipmentSlot, previous, next) -> {
            KessokuTestLifecycle.LOGGER.info("[SERVER] Entity equipment change: Entity: {}, Slot {}, Previous: {}, Current {} ", livingEntity, equipmentSlot.name(), previous, next);
        }));
    }
}
