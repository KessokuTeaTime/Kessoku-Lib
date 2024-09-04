package kessoku.testmod.lifecycle;

import band.kessoku.lib.entrypoint.api.KessokuModInitializer;
import band.kessoku.lib.events.lifecycle.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KessokuTestLifecycle implements KessokuModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("LifecycleEventsTest");

    @Override
    public void onInitialize() {
        //Server Chunk
        ServerChunkEvent.LOADED.register(((serverWorld, world) -> {
            //Do something
        }));
        ServerChunkEvent.UNLOADED.register(((serverWorld, world) -> {
            //Do something
        }));

        //Server Lifecycle
        ServerLifecycleEvent.STARTED.register(((minecraftServer) -> {
            LOGGER.info("Started Server!");
        }));
        ServerLifecycleEvent.STARTING.register(((minecraftServer) -> {
            LOGGER.info("Starting Server!");
        }));
        ServerLifecycleEvent.STOPPED.register(((minecraftServer) -> {
            LOGGER.info("Stopping Server!");
        }));
        ServerLifecycleEvent.STOPPING.register(((minecraftServer) -> {
            LOGGER.info("Stopped Server!");
        }));
        ServerLifecycleEvent.SYNC_DATA_PACK_CONTENTS.register(((player, joined) -> {
            LOGGER.info("SyncDataPackContents received for {}", joined ? "join" : "reload");
        }));
        ServerLifecycleEvent.BEFORE_SAVE.register((server, flush, force) -> {
            LOGGER.info("Starting Save with settings: Flush:{} Force:{}", flush, force);
        });
        ServerLifecycleEvent.AFTER_SAVE.register((server, flush, force) -> {
            LOGGER.info("Save Finished with settings: Flush:{} Force:{}", flush, force);
        });
    }
}
