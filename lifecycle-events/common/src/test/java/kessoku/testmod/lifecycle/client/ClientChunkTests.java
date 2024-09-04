package kessoku.testmod.lifecycle.client;

import band.kessoku.lib.entrypoint.api.KessokuClientModInitializer;
import band.kessoku.lib.events.lifecycle.api.client.ClientChunkEvent;

public class ClientChunkTests implements KessokuClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientChunkEvent.LOADED.register((world, chunk) -> {
            //Do something in client
        });

        ClientChunkEvent.UNLOADED.register((world, chunk) -> {
            //Do something in client
        });
    }
}
