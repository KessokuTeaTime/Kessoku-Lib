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
package band.kessoku.lib.impl.event.lifecycle.fabric;

import band.kessoku.lib.api.event.lifecycle.*;
import band.kessoku.lib.api.event.lifecycle.client.*;
import band.kessoku.lib.impl.event.lifecycle.KessokuLifecycleEvents;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.*;

public class KessokuLifecycleEventsImplFabric {
    public static void registerClientEvents() {
        KessokuLifecycleEvents.clientInit();

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> ClientLifecycleEvent.STARTED.invoker().onClientStarted(client));
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> ClientLifecycleEvent.STOPPING.invoker().onClientStopping(client));

        ClientTickEvents.START_CLIENT_TICK.register(client -> ClientTickEvent.START_CLIENT_TICK.invoker().onStartTick(client));
        ClientTickEvents.END_CLIENT_TICK.register(client -> ClientTickEvent.END_CLIENT_TICK.invoker().onEndTick(client));
        ClientTickEvents.START_WORLD_TICK.register(world -> ClientTickEvent.START_WORLD_TICK.invoker().onStartTick(world));
        ClientTickEvents.END_WORLD_TICK.register(world -> ClientTickEvent.END_WORLD_TICK.invoker().onEndTick(world));

        ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> ClientEntityEvent.LOADED.invoker().onLoaded(entity, world));
        ClientEntityEvents.ENTITY_UNLOAD.register((entity, world) -> ClientEntityEvent.UNLOADED.invoker().onUnloaded(entity, world));

        ClientChunkEvents.CHUNK_LOAD.register((world, chunk) -> ClientChunkEvent.LOADED.invoker().onChunkLoaded(world, chunk));
        ClientChunkEvents.CHUNK_UNLOAD.register((world, chunk) -> ClientChunkEvent.UNLOADED.invoker().onChunkUnloaded(world, chunk));

        ClientBlockEntityEvents.BLOCK_ENTITY_LOAD.register((blockEntity, world) -> ClientBlockEntityEvent.LOADED.invoker().onLoaded(blockEntity, world));
        ClientBlockEntityEvents.BLOCK_ENTITY_UNLOAD.register((blockEntity, world) -> ClientBlockEntityEvent.UNLOADED.invoker().onUnloaded(blockEntity, world));
    }

    public static void registerCommonEvents() {
        KessokuLifecycleEvents.init();

        CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> LifecycleEvent.TAG_LOADED.invoker().onTagsLoaded(registries, client));

        ServerLifecycleEvents.SERVER_STARTING.register(server -> ServerLifecycleEvent.STARTING.invoker().onServerStarting(server));
        ServerLifecycleEvents.SERVER_STARTED.register(server -> ServerLifecycleEvent.STARTED.invoker().onServerStarted(server));
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> ServerLifecycleEvent.STOPPING.invoker().onServerStopping(server));
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> ServerLifecycleEvent.STOPPED.invoker().onServerStopped(server));

        ServerTickEvents.START_SERVER_TICK.register(server -> ServerTickEvent.START_SERVER_TICK.invoker().onStartTick(server));
        ServerTickEvents.END_SERVER_TICK.register(server -> ServerTickEvent.END_SERVER_TICK.invoker().onEndTick(server));
        ServerTickEvents.START_WORLD_TICK.register(world -> ServerTickEvent.START_WORLD_TICK.invoker().onStartTick(world));
        ServerTickEvents.END_WORLD_TICK.register(world -> ServerTickEvent.END_WORLD_TICK.invoker().onEndTick(world));

        ServerWorldEvents.LOAD.register((server, world) -> ServerWorldEvent.LOADED.invoker().onWorldLoaded(server, world));
        ServerWorldEvents.UNLOAD.register((server, world) -> ServerWorldEvent.UNLOADED.invoker().onWorldUnloaded(server, world));

        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) -> ServerLifecycleEvent.SYNC_DATA_PACK_CONTENTS.invoker().onSyncDataPackContents(player, joined));
        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((server, resourceManager) -> ServerLifecycleEvent.START_DATA_PACK_RELOAD.invoker().startDataPackReload(server, resourceManager));
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> ServerLifecycleEvent.END_DATA_PACK_RELOAD.invoker().endDataPackReload(server, resourceManager, success));

        ServerLifecycleEvents.BEFORE_SAVE.register((server, flush, force) -> ServerLifecycleEvent.BEFORE_SAVE.invoker().onBeforeSaveData(server, flush, force));
        ServerLifecycleEvents.AFTER_SAVE.register((server, flush, force) -> ServerLifecycleEvent.AFTER_SAVE.invoker().onAfterSaveData(server, flush, force));

        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> ServerEntityEvent.LOADED.invoker().onLoaded(entity, world));
        ServerEntityEvents.ENTITY_UNLOAD.register((entity, world) -> ServerEntityEvent.UNLOADED.invoker().onUnloaded(entity, world));
        ServerEntityEvents.EQUIPMENT_CHANGE.register((livingEntity, equipmentSlot, previousStack, currentStack) -> ServerEntityEvent.EQUIPMENT_CHANGED.invoker().onChanged(livingEntity, equipmentSlot, previousStack, currentStack));

        ServerChunkEvents.CHUNK_LOAD.register((world, chunk) -> ServerChunkEvent.LOADED.invoker().onChunkLoaded(world, chunk));
        ServerChunkEvents.CHUNK_UNLOAD.register((world, chunk) -> ServerChunkEvent.UNLOADED.invoker().onChunkUnloaded(world, chunk));

        ServerBlockEntityEvents.BLOCK_ENTITY_LOAD.register((blockEntity, world) -> ServerBlockEntityEvent.LOADED.invoker().onLoaded(blockEntity, world));
        ServerBlockEntityEvents.BLOCK_ENTITY_UNLOAD.register((blockEntity, world) -> ServerBlockEntityEvent.UNLOADED.invoker().onUnloaded(blockEntity, world));
    }
}
