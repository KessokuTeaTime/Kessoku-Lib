package band.kessoku.lib.events.lifecycle;

import band.kessoku.lib.events.lifecycle.impl.KessokuLifecycleEventsImpl;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(KessokuLifecycleEvents.MOD_ID)
public class KessokuLifecycleEventsEntrypoint {
    public KessokuLifecycleEventsEntrypoint(IEventBus modEventBus, ModContainer modContainer) {
        var forgeEventBus = NeoForge.EVENT_BUS;

        KessokuLifecycleEventsImpl.registerCommonEvents(modEventBus, forgeEventBus);
    }
}
