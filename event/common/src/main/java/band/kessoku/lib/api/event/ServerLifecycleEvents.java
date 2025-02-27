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


import net.minecraft.resource.LifecycledResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

public class ServerLifecycleEvents {
    private ServerLifecycleEvents() {
    }

    /**
     * Called when a Minecraft server is starting.
     *
     * <p>This occurs before the {@link PlayerManager player manager} and any worlds are loaded.
     */
    public static final Event<ServerStarting> STARTING = Event.of(callbacks -> server -> {
        for (ServerStarting callback : callbacks) {
            callback.onServerStarting(server);
        }
    });

    /**
     * Called when a Minecraft server has started and is about to tick for the first time.
     *
     * <p>At this stage, all worlds are live.
     */
    public static final Event<ServerStarted> STARTED = Event.of(callbacks -> server -> {
        for (ServerStarted callback : callbacks) {
            callback.onServerStarted(server);
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
    public static final Event<ServerStopping> STOPPING = Event.of(callbacks -> server -> {
        for (ServerStopping callback : callbacks) {
            callback.onServerStopping(server);
        }
    });

    /**
     * Called when a Minecraft server has stopped.
     * All worlds have been closed and all (block)entities and players have been unloaded.
     *
     * <p>For example, an {@link net.fabricmc.api.EnvType#CLIENT integrated server} will begin stopping, but its client may continue to run.
     * Meanwhile, for a {@link net.fabricmc.api.EnvType#SERVER dedicated server}, this will be the last event called.
     */
    public static final Event<ServerStopped> STOPPED = Event.of(callbacks -> server -> {
        for (ServerStopped callback : callbacks) {
            callback.onServerStopped(server);
        }
    });

    /**
     * Called when a Minecraft server is about to send tag and recipe data to a player.
     *
     * @see SyncDataPackContents
     */
    public static final Event<SyncDataPackContents> SYNC_DATA_PACK_CONTENTS = Event.of(callbacks -> (player, joined) -> {
        for (SyncDataPackContents callback : callbacks) {
            callback.onSyncDataPackContents(player, joined);
        }
    });

    /**
     * Called before a Minecraft server reloads data packs.
     */
    public static final Event<StartDataPackReload> START_DATA_PACK_RELOAD = Event.of(callbacks -> (server, serverResourceManager) -> {
        for (StartDataPackReload callback : callbacks) {
            callback.startDataPackReload(server, serverResourceManager);
        }
    });

    /**
     * Called after a Minecraft server has reloaded data packs.
     *
     * <p>If reloading data packs was unsuccessful, the current data packs will be kept.
     */
    public static final Event<EndDataPackReload> END_DATA_PACK_RELOAD = Event.of(callbacks -> (server, serverResourceManager, success) -> {
        for (EndDataPackReload callback : callbacks) {
            callback.endDataPackReload(server, serverResourceManager, success);
        }
    });

    /**
     * Called before a Minecraft server begins saving data.
     */
    public static final Event<BeforeSave> BEFORE_SAVE = Event.of(callbacks -> (server, flush, force) -> {
        for (BeforeSave callback : callbacks) {
            callback.onBeforeSave(server, flush, force);
        }
    });

    /**
     * Called after a Minecraft server finishes saving data.
     */
    public static final Event<AfterSave> AFTER_SAVE = Event.of(callbacks -> (server, flush, force) -> {
        for (AfterSave callback : callbacks) {
            callback.onAfterSave(server, flush, force);
        }
    });

    @FunctionalInterface
    public interface ServerStarting {
        void onServerStarting(MinecraftServer server);
    }

    @FunctionalInterface
    public interface ServerStarted {
        void onServerStarted(MinecraftServer server);
    }

    @FunctionalInterface
    public interface ServerStopping {
        void onServerStopping(MinecraftServer server);
    }

    @FunctionalInterface
    public interface ServerStopped {
        void onServerStopped(MinecraftServer server);
    }

    @FunctionalInterface
    public interface SyncDataPackContents {
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
    public interface StartDataPackReload {
        void startDataPackReload(MinecraftServer server, LifecycledResourceManager resourceManager);
    }

    @FunctionalInterface
    public interface EndDataPackReload {
        /**
         * Called after data packs on a Minecraft server have been reloaded.
         *
         * <p>If the reload was not successful, the old data packs will be kept.
         *
         * @param server          the server
         * @param resourceManager the resource manager
         * @param success         if the reload was successful
         */
        void endDataPackReload(MinecraftServer server, LifecycledResourceManager resourceManager, boolean success);
    }

    @FunctionalInterface
    public interface BeforeSave {
        /**
         * Called before a Minecraft server begins saving data.
         *
         * @param server the server
         * @param flush  is true when all chunks are being written to disk, server will likely freeze during this time
         * @param force  whether servers that have save-off set should save
         */
        void onBeforeSave(MinecraftServer server, boolean flush, boolean force);
    }

    @FunctionalInterface
    public interface AfterSave {
        /**
         * Called before a Minecraft server begins saving data.
         *
         * @param server the server
         * @param flush  is true when all chunks are being written to disk, server will likely freeze during this time
         * @param force  whether servers that have save-off set should save
         */
        void onAfterSave(MinecraftServer server, boolean flush, boolean force);
    }
}
