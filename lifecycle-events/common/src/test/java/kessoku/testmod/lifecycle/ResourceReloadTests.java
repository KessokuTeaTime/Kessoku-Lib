package kessoku.testmod.lifecycle;

import band.kessoku.lib.entrypoint.api.KessokuModInitializer;
import band.kessoku.lib.events.lifecycle.api.ServerLifecycleEvent;

public class ResourceReloadTests implements KessokuModInitializer {
    @Override
    public void onInitialize() {
        ServerLifecycleEvent.START_DATA_PACK_RELOAD.register(((server, serverResourceManager) -> {
            KessokuTestLifecycle.LOGGER.info("PREPARING FOR RELOAD");
        }));

        ServerLifecycleEvent.END_DATA_PACK_RELOAD.register(((server, serverResourceManager, success) -> {
            if (success) {
                KessokuTestLifecycle.LOGGER.info("FINISHED RELOAD on {}", Thread.currentThread());
            } else {
                // Failure can be tested by trying to disable the vanilla datapack
                KessokuTestLifecycle.LOGGER.error("FAILED TO RELOAD on {}", Thread.currentThread());
            }
        }));
    }
}
