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
package band.kessoku.lib.impl.event.lifecycle.neoforge;

import band.kessoku.lib.api.base.neoforge.NeoEventUtils;
import band.kessoku.lib.api.event.lifecycle.*;
import band.kessoku.lib.api.event.lifecycle.client.ClientChunkEvent;
import band.kessoku.lib.impl.event.lifecycle.KessokuLifecycleEvents;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.WorldChunk;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

public class KessokuLifecycleEventsImplNeo {
    public static void registerClientEvents(IEventBus forgeEventBus) {
        KessokuLifecycleEvents.clientInit();

        NeoEventUtils.registerEvent(forgeEventBus, ChunkEvent.Load.class, event -> {
            if (event.getLevel() instanceof ClientWorld world && event.getChunk() instanceof WorldChunk chunk) {
                ClientChunkEvent.LOADED.invoker().onChunkLoaded(world, chunk);
            }
        });
        NeoEventUtils.registerEvent(forgeEventBus, ChunkEvent.Unload.class, event -> {
            if (event.getLevel() instanceof ClientWorld world && event.getChunk() instanceof WorldChunk chunk) {
                ClientChunkEvent.UNLOADED.invoker().onChunkUnloaded(world, chunk);
            }
        });

        NeoEventUtils.registerEvent(forgeEventBus, ClientTickEvent.Pre.class, event -> {
            band.kessoku.lib.api.event.lifecycle.client.ClientTickEvent.START_CLIENT_TICK.invoker().onStartTick(MinecraftClient.getInstance());
        });
        NeoEventUtils.registerEvent(forgeEventBus, ClientTickEvent.Post.class, event -> {
            band.kessoku.lib.api.event.lifecycle.client.ClientTickEvent.END_CLIENT_TICK.invoker().onEndTick(MinecraftClient.getInstance());
        });

        NeoEventUtils.registerEvent(forgeEventBus, LevelTickEvent.Pre.class, event -> {
            if (event.getLevel() instanceof ClientWorld world) {
                band.kessoku.lib.api.event.lifecycle.client.ClientTickEvent.START_WORLD_TICK.invoker().onStartTick(world);
            }
        });
        NeoEventUtils.registerEvent(forgeEventBus, LevelTickEvent.Post.class, event -> {
            if (event.getLevel() instanceof ClientWorld world) {
                band.kessoku.lib.api.event.lifecycle.client.ClientTickEvent.END_WORLD_TICK.invoker().onEndTick(world);
            }
        });
    }

    public static void registerCommonEvents(IEventBus forgeEventBus) {
        KessokuLifecycleEvents.init();

        NeoEventUtils.registerEvent(forgeEventBus, TagsUpdatedEvent.class, event -> {
            LifecycleEvent.TAG_LOADED.invoker().onTagsLoaded(event.getRegistryAccess(), event.getUpdateCause() == TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED);
        });

        NeoEventUtils.registerEvent(forgeEventBus, ChunkEvent.Load.class, event -> {
            if (event.getLevel() instanceof ServerWorld world && event.getChunk() instanceof WorldChunk chunk) {
                ServerChunkEvent.LOADED.invoker().onChunkLoaded(world, chunk);
            }
        });
        NeoEventUtils.registerEvent(forgeEventBus, ChunkEvent.Unload.class, event -> {
            if (event.getLevel() instanceof ServerWorld world && event.getChunk() instanceof WorldChunk chunk) {
                ServerChunkEvent.UNLOADED.invoker().onChunkUnloaded(world, chunk);
            }
        });

        NeoEventUtils.registerEvent(forgeEventBus, ServerStartingEvent.class, event -> {
            ServerLifecycleEvent.STARTING.invoker().onServerStarting(event.getServer());
        });
        NeoEventUtils.registerEvent(forgeEventBus, ServerStartedEvent.class, event -> {
            ServerLifecycleEvent.STARTED.invoker().onServerStarted(event.getServer());
        });
        NeoEventUtils.registerEvent(forgeEventBus, ServerStoppingEvent.class, event -> {
            ServerLifecycleEvent.STOPPING.invoker().onServerStopping(event.getServer());
        });
        NeoEventUtils.registerEvent(forgeEventBus, ServerStoppedEvent.class, event -> {
            ServerLifecycleEvent.STOPPED.invoker().onServerStopped(event.getServer());
        });

        NeoEventUtils.registerEvent(forgeEventBus, net.neoforged.neoforge.event.tick.ServerTickEvent.Pre.class, event -> {
            ServerTickEvent.START_SERVER_TICK.invoker().onStartTick(event.getServer());
        });
        NeoEventUtils.registerEvent(forgeEventBus, net.neoforged.neoforge.event.tick.ServerTickEvent.Post.class, event -> {
            ServerTickEvent.END_SERVER_TICK.invoker().onEndTick(event.getServer());
        });
//        NeoEventUtils.registerEvent(forgeEventBus, LevelTickEvent.Pre.class, event -> {
//            if (event.getLevel() instanceof ServerWorld world) {
//                ServerTickEvent.START_WORLD_TICK.invoker().onStartTick(world);
//            }
//        });
//        NeoEventUtils.registerEvent(forgeEventBus, LevelTickEvent.Post.class, event -> {
//            if (event.getLevel() instanceof ServerWorld world) {
//                ServerTickEvent.END_WORLD_TICK.invoker().onEndTick(world);
//            }
//        });

        NeoEventUtils.registerEvent(forgeEventBus, LevelEvent.Load.class, event -> {
            if (event.getLevel() instanceof ServerWorld world) {
                ServerWorldEvent.LOADED.invoker().onWorldLoaded(world.getServer(), world);
            }
        });
        NeoEventUtils.registerEvent(forgeEventBus, LevelEvent.Unload.class, event -> {
            if (event.getLevel() instanceof ServerWorld world) {
                ServerWorldEvent.UNLOADED.invoker().onWorldUnloaded(world.getServer(), world);
            }
        });

        NeoEventUtils.registerEvent(forgeEventBus, LivingEquipmentChangeEvent.class, event -> {
            ServerEntityEvent.EQUIPMENT_CHANGED.invoker().onChanged(event.getEntity(), event.getSlot(), event.getFrom(), event.getTo());
        });

        NeoEventUtils.registerEvent(forgeEventBus, OnDatapackSyncEvent.class, event -> {
            if (event.getPlayer() != null) {
                ServerLifecycleEvent.SYNC_DATA_PACK_CONTENTS.invoker().onSyncDataPackContents(event.getPlayer(), true);
            } else {
                for (ServerPlayerEntity player : event.getPlayerList().getPlayerList()) {
                    ServerLifecycleEvent.SYNC_DATA_PACK_CONTENTS.invoker().onSyncDataPackContents(player, false);
                }
            }
        });
    }
}
