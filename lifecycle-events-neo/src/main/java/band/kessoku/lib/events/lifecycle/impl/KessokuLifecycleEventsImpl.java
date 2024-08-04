package band.kessoku.lib.events.lifecycle.impl;

import band.kessoku.lib.event.util.NeoEventUtils;
import band.kessoku.lib.events.lifecycle.api.LifecycleEvents;
import band.kessoku.lib.events.lifecycle.api.server.ServerLifecycleEvents;
import band.kessoku.lib.events.lifecycle.api.server.ServerTickEvents;
import band.kessoku.lib.events.lifecycle.api.server.ServerWorldEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public class KessokuLifecycleEventsImpl {
    public static void registerClientEvents(IEventBus modEventBus, IEventBus forgeEventBus) {

    }

    public static void registerCommonEvents(IEventBus modEventBus, IEventBus forgeEventBus) {
        NeoEventUtils.registerEvent(forgeEventBus, TagsUpdatedEvent.class, event -> {
            LifecycleEvents.TAG_LOADED.invoker().onTagsLoaded(event.getRegistryAccess(), event.getUpdateCause() == TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED);
        });

        NeoEventUtils.registerEvent(forgeEventBus, ServerStartingEvent.class, event -> {
            ServerLifecycleEvents.STARTING.invoker().onServerStarting(event.getServer());
        });
        NeoEventUtils.registerEvent(forgeEventBus, ServerStartedEvent.class, event -> {
            ServerLifecycleEvents.STARTED.invoker().onServerStarted(event.getServer());
        });
        NeoEventUtils.registerEvent(forgeEventBus, ServerStoppingEvent.class, event -> {
            ServerLifecycleEvents.STOPPING.invoker().onServerStopping(event.getServer());
        });
        NeoEventUtils.registerEvent(forgeEventBus, ServerStoppedEvent.class, event -> {
            ServerLifecycleEvents.STOPPED.invoker().onServerStopped(event.getServer());
        });

        NeoEventUtils.registerEvent(forgeEventBus, ServerTickEvent.Pre.class, event -> {
            ServerTickEvents.START_SERVER_TICK.invoker().onStartTick(event.getServer());
        });
        NeoEventUtils.registerEvent(forgeEventBus, ServerTickEvent.Post.class, event -> {
            ServerTickEvents.END_SERVER_TICK.invoker().onEndTick(event.getServer());
        });
        NeoEventUtils.registerEvent(forgeEventBus, LevelTickEvent.Pre.class, event -> {
            if (event.getLevel() instanceof ServerWorld world) {
                ServerTickEvents.START_WORLD_TICK.invoker().onStartTick(world);
            }
        });
        NeoEventUtils.registerEvent(forgeEventBus, LevelTickEvent.Post.class, event -> {
            if (event.getLevel() instanceof ServerWorld world) {
                ServerTickEvents.END_WORLD_TICK.invoker().onEndTick(world);
            }
        });

        NeoEventUtils.registerEvent(forgeEventBus, LevelEvent.Load.class, event -> {
            if (event.getLevel() instanceof ServerWorld world) {
                ServerWorldEvents.LOADED.invoker().onWorldLoaded(world.getServer(), world);
            }
        });
        NeoEventUtils.registerEvent(forgeEventBus, LevelEvent.Unload.class, event -> {
            if (event.getLevel() instanceof ServerWorld world) {
                ServerWorldEvents.UNLOADED.invoker().onWorldUnloaded(world.getServer(), world);
            }
        });

        NeoEventUtils.registerEvent(forgeEventBus, OnDatapackSyncEvent.class, event -> {
            if (event.getPlayer() != null) {
                ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.invoker().onSyncDataPackContents(event.getPlayer(), true);
            } else {
                for (ServerPlayerEntity player : event.getPlayerList().getPlayerList()) {
                    ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.invoker().onSyncDataPackContents(player, false);
                }
            }
        });
    }
}
