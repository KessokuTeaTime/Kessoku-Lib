package band.kessoku.lib.events.lifecycle;

import band.kessoku.lib.events.lifecycle.impl.KessokuLifecycleEventsImplFabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class KessokuLifecycleEventsEntrypoint implements ModInitializer, ClientModInitializer {
    @Override
    public void onInitialize() {
        KessokuLifecycleEventsImplFabric.registerCommonEvents();
    }

    @Override
    public void onInitializeClient() {
        KessokuLifecycleEventsImplFabric.registerClientEvents();
    }
}
