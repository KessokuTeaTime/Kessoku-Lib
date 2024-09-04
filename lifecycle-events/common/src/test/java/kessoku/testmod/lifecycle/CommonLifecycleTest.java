package kessoku.testmod.lifecycle;

import band.kessoku.lib.entrypoint.api.KessokuModInitializer;
import band.kessoku.lib.events.lifecycle.api.LifecycleEvent;

public class CommonLifecycleTest implements KessokuModInitializer {
    @Override
    public void onInitialize() {
        LifecycleEvent.TAG_LOADED.register(((registries, client) -> {
            KessokuTestLifecycle.LOGGER.info("Tags (re)loaded on {} {}", client ? "client" : "server", Thread.currentThread());
        }));
    }
}
