package band.kessoku.lib.events.lifecycle;

import band.kessoku.lib.base.ModUtils;
import band.kessoku.lib.events.lifecycle.impl.KessokuLifecycleEventsImplNeo;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;

@Mod(KessokuLifecycleEvents.MOD_ID)
public class KessokuLifecycleEventsEntrypoint {
    public KessokuLifecycleEventsEntrypoint(IEventBus modEventBus, ModContainer modContainer) {
        var forgeEventBus = NeoForge.EVENT_BUS;
        ModUtils.getLogger().info(KessokuLifecycleEvents.MARKER, "KessokuLib-LifecycleEvents is loaded!");
        KessokuLifecycleEventsImplNeo.registerCommonEvents(forgeEventBus);
        if (FMLLoader.getDist().isClient()) {
            ModUtils.getLogger().info(KessokuLifecycleEvents.MARKER, "KessokuLib-LifecycleEvents is loaded on client!");
            KessokuLifecycleEventsImplNeo.registerClientEvents(forgeEventBus);
        }
    }
}
