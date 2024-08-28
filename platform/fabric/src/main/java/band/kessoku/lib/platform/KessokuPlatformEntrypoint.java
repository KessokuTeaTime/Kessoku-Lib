package band.kessoku.lib.platform;

import band.kessoku.lib.base.ModUtils;

import net.fabricmc.api.ModInitializer;

public class KessokuPlatformEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        ModUtils.getLogger().info(KessokuPlatform.MARKER, "KessokuLib-Platform is loaded!");
    }
}
