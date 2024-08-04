package band.kessoku.lib.events.lifecycle.api.server;

import band.kessoku.lib.event.api.Event;
import net.minecraft.resource.LifecycledResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class ServerLifecycleEvents {

    /**
     * Called when a Minecraft server is starting.
     *
     * <p>This occurs before the {@link net.minecraft.server.PlayerManager player manager} and any worlds are loaded.
     */
    public static final Event<Server.Starting> STARTING = Event.of(startings -> server ->  {
        for (Server.Starting serverStarting : startings) {
            serverStarting.onServerStarting(server);
        }
    });

    /**
     * Called when a Minecraft server has started and is about to tick for the first time.
     *
     * <p>At this stage, all worlds are live.
     */
    public static final Event<Server.Started> STARTED = Event.of(starteds -> server ->  {
        for (Server.Started serverStarted : starteds) {
            serverStarted.onServerStarted(server);
        }
    });

    /**
     * Called when a Minecraft server has started shutting down.
     * This occurs before the server's network channel is closed and before any players are disconnected.
     *
     * <p>For example, an integrated server will begin stopping, but its client may continue to run.
     *
     * <p>All worlds are still present and can be modified.
     */
    public static final Event<Server.Stopping> STOPPING = Event.of(stoppings -> server ->  {
        for (Server.Stopping serverStopping : stoppings) {
            serverStopping.onServerStopping(server);
        }
    });

    /**
     * Called when a Minecraft server has stopped.
     * All worlds have been closed and all (block)entities and players have been unloaded.
     *
     * <p>For example, an {@link net.fabricmc.api.EnvType#CLIENT integrated server} will begin stopping, but its client may continue to run.
     * Meanwhile, for a {@link net.fabricmc.api.EnvType#SERVER dedicated server}, this will be the last event called.
     */
    public static final Event<Server.Stopped> STOPPED = Event.of(stoppeds -> server ->  {
        for (Server.Stopped serverStopped : stoppeds) {
            serverStopped.onServerStopped(server);
        }
    });

    /**
     * Called when a Minecraft server is about to send tag and recipe data to a player.
     * @see Datapack.SyncContents
     */
    public static final Event<Datapack.SyncContents> SYNC_DATA_PACK_CONTENTS = Event.of(syncContents -> (player, joined) ->  {
        for (Datapack.SyncContents syncDataPackContents : syncContents) {
            syncDataPackContents.onSyncDataPackContents(player, joined);
        }
    });

    /**
     * Called before a Minecraft server reloads data packs.
     */
    public static final Event<Datapack.StartReload> START_DATA_PACK_RELOAD = Event.of(startReloads -> (server, serverResourceManager) -> {
        for (Datapack.StartReload startDataPackReload : startReloads) {
            startDataPackReload.startDataPackReload(server, serverResourceManager);
        }
    });

    /**
     * Called after a Minecraft server has reloaded data packs.
     *
     * <p>If reloading data packs was unsuccessful, the current data packs will be kept.
     */
    public static final Event<Datapack.EndReload> END_DATA_PACK_RELOAD = Event.of(endReloads -> (server, serverResourceManager, success) -> {
        for (Datapack.EndReload endDataPackReload : endReloads) {
            endDataPackReload.endDataPackReload(server, serverResourceManager, success);
        }
    });

    /**
     * Called before a Minecraft server begins saving data.
     */
    public static final Event<SaveData.Before> BEFORE_SAVE = Event.of(befores -> (server, flush, force) -> {
        for (SaveData.Before beforeSaveData : befores) {
            beforeSaveData.onBeforeSaveData(server, flush, force);
        }
    });

    /**
     * Called after a Minecraft server finishes saving data.
     */
    public static final Event<SaveData.After> AFTER_SAVE = Event.of(afters -> (server, flush, force) -> {
        for (SaveData.After afterSaveData : afters) {
            afterSaveData.onAfterSaveData(server, flush, force);
        }
    });

    public interface Server {
        @FunctionalInterface
        interface Starting {
            void onServerStarting(MinecraftServer server);
        }

        @FunctionalInterface
        interface Started {
            void onServerStarted(MinecraftServer server);
        }

        @FunctionalInterface
        interface Stopping {
            void onServerStopping(MinecraftServer server);
        }

        @FunctionalInterface
        interface Stopped {
            void onServerStopped(MinecraftServer server);
        }
    }

    public interface Datapack {
        @FunctionalInterface
        interface SyncContents {
            /**
             * Called right before tags and recipes are sent to a player,
             * either because the player joined, or because the server reloaded resources.
             * The {@linkplain MinecraftServer#getResourceManager() server resource manager} is up-to-date when this is called.
             *
             * <p>For example, this event can be used to sync data loaded with custom resource reloaders.
             *
             * @param player Player to which the data is being sent.
             * @param joined True if the player is joining the server, false if the server finished a successful resource reload.
             */
            void onSyncDataPackContents(ServerPlayerEntity player, boolean joined);
        }

        @FunctionalInterface
        interface StartReload {
            void startDataPackReload(MinecraftServer server, LifecycledResourceManager resourceManager);
        }

        @FunctionalInterface
        interface EndReload {
            /**
             * Called after data packs on a Minecraft server have been reloaded.
             *
             * <p>If the reload was not successful, the old data packs will be kept.
             *
             * @param server the server
             * @param resourceManager the resource manager
             * @param success if the reload was successful
             */
            void endDataPackReload(MinecraftServer server, LifecycledResourceManager resourceManager, boolean success);
        }
    }

    public interface SaveData {
        @FunctionalInterface
        interface Before {
            /**
             * Called before a Minecraft server begins saving data.
             *
             * @param server the server
             * @param flush is true when all chunks are being written to disk, server will likely freeze during this time
             * @param force whether servers that have save-off set should save
             */
            void onBeforeSaveData(MinecraftServer server, boolean flush, boolean force);
        }

        @FunctionalInterface
        interface After {
            /**
             * Called after a Minecraft server begins saving data.
             *
             * @param server the server
             * @param flush is true when all chunks are being written to disk, server will likely freeze during this time
             * @param force whether servers that have save-off set should save
             */
            void onAfterSaveData(MinecraftServer server, boolean flush, boolean force);
        }
    }


}
