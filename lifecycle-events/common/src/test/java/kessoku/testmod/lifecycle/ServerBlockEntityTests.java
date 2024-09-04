package kessoku.testmod.lifecycle;

import band.kessoku.lib.entrypoint.api.KessokuModInitializer;
import band.kessoku.lib.events.lifecycle.api.ServerBlockEntityEvent;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.registry.Registries;

import java.util.ArrayList;
import java.util.List;

public class ServerBlockEntityTests implements KessokuModInitializer {
    private final List<BlockEntity> serverBlockEntities = new ArrayList<>();

    @Override
    public void onInitialize() {
        ServerBlockEntityEvent.LOADED.register(((blockEntity, world) -> {
            this.serverBlockEntities.add(blockEntity);

            KessokuTestLifecycle.LOGGER.info("[SERVER] LOADED {} - BlockEntities: {}", Registries.BLOCK_ENTITY_TYPE.getId(blockEntity.getType()).toString(), this.serverBlockEntities.size());
        }));

        ServerBlockEntityEvent.UNLOADED.register(((blockEntity, world) -> {
            this.serverBlockEntities.remove(blockEntity);

            KessokuTestLifecycle.LOGGER.info("[SERVER] UNLOADED {} - BlockEntities: {}", Registries.BLOCK_ENTITY_TYPE.getId(blockEntity.getType()).toString(), this.serverBlockEntities.size());
        }));
    }
}
