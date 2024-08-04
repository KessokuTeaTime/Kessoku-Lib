package band.kessoku.lib.events.lifecycle;

import band.kessoku.lib.events.lifecycle.impl.KessokuLifecycleEventsImpl;
import net.fabricmc.api.ModInitializer;

public class KessokuLifecycleEventsEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        KessokuLifecycleEventsImpl.registerCommonEvents();
    }
}
