package band.kessoku.lib.events.lifecycle;

import band.kessoku.lib.base.ModUtils;
import band.kessoku.lib.events.lifecycle.impl.KessokuLifecycleEventsImplFabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class KessokuLifecycleEventsEntrypoint implements ModInitializer, ClientModInitializer {
    @Override
    public void onInitialize() {
        ModUtils.getLogger().info(KessokuLifecycleEvents.MARKER, "KessokuLib-LifecycleEvents is loaded!");
        KessokuLifecycleEventsImplFabric.registerCommonEvents();
    }

    @Override
    public void onInitializeClient() {
        ModUtils.getLogger().info(KessokuLifecycleEvents.MARKER, "KessokuLib-LifecycleEvents is loaded on client!");
        KessokuLifecycleEventsImplFabric.registerClientEvents();
    }
}
