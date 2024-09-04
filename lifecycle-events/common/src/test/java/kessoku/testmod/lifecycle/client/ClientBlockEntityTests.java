package kessoku.testmod.lifecycle.client;

import band.kessoku.lib.entrypoint.api.KessokuClientModInitializer;
import band.kessoku.lib.events.lifecycle.api.client.ClientBlockEntityEvent;

public class ClientBlockEntityTests implements KessokuClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientBlockEntityEvent.LOADED.register((blockEntity, world) -> {
            //Do something in client
        });

        ClientBlockEntityEvent.UNLOADED.register((blockEntity, world) -> {
            //Do something in client
        });
    }
}
