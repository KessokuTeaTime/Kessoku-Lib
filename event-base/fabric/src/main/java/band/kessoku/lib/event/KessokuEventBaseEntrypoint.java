package band.kessoku.lib.event;

import band.kessoku.lib.base.ModUtils;

import net.fabricmc.api.ModInitializer;

public class KessokuEventBaseEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        ModUtils.getLogger().info(KessokuEventBase.MARKER, "KessokuLib-EventBase is Loaded!");
    }
}
