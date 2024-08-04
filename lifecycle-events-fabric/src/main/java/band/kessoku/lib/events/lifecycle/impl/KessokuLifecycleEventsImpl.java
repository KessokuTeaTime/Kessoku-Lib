package band.kessoku.lib.events.lifecycle.impl;

import band.kessoku.lib.events.lifecycle.api.LifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;

public class KessokuLifecycleEventsImpl {
    public static void registerCommonEvents() {
        CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> LifecycleEvents.TAG_LOADED.invoker().onTagsLoaded(registries, client));

        ServerLifecycleEvents.SERVER_STARTING.register(server -> band.kessoku.lib.events.lifecycle.api.server.ServerLifecycleEvents.STARTING.invoker().onServerStarting(server));
        ServerLifecycleEvents.SERVER_STARTED.register(server -> band.kessoku.lib.events.lifecycle.api.server.ServerLifecycleEvents.STARTED.invoker().onServerStarted(server));
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> band.kessoku.lib.events.lifecycle.api.server.ServerLifecycleEvents.STOPPING.invoker().onServerStopping(server));
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> band.kessoku.lib.events.lifecycle.api.server.ServerLifecycleEvents.STOPPED.invoker().onServerStopped(server));

        ServerTickEvents.START_SERVER_TICK.register(server -> band.kessoku.lib.events.lifecycle.api.server.ServerTickEvents.START_SERVER_TICK.invoker().onStartTick(server));
        ServerTickEvents.END_SERVER_TICK.register(server -> band.kessoku.lib.events.lifecycle.api.server.ServerTickEvents.END_SERVER_TICK.invoker().onEndTick(server));
        ServerTickEvents.START_WORLD_TICK.register(world -> band.kessoku.lib.events.lifecycle.api.server.ServerTickEvents.START_WORLD_TICK.invoker().onStartTick(world));
        ServerTickEvents.END_WORLD_TICK.register(world -> band.kessoku.lib.events.lifecycle.api.server.ServerTickEvents.END_WORLD_TICK.invoker().onEndTick(world));

        ServerWorldEvents.LOAD.register((server, world) -> band.kessoku.lib.events.lifecycle.api.server.ServerWorldEvents.LOADED.invoker().onWorldLoaded(server, world));
        ServerWorldEvents.UNLOAD.register((server, world) -> band.kessoku.lib.events.lifecycle.api.server.ServerWorldEvents.UNLOADED.invoker().onWorldUnloaded(server, world));

        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) -> band.kessoku.lib.events.lifecycle.api.server.ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.invoker().onSyncDataPackContents(player, joined));
    }
}
