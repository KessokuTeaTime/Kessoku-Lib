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

import java.util.ArrayList;
import java.util.List;

import band.kessoku.lib.api.entrypoint.entrypoints.KessokuModInitializer;
import band.kessoku.lib.events.lifecycle.api.ServerBlockEntityEvent;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.registry.Registries;

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
