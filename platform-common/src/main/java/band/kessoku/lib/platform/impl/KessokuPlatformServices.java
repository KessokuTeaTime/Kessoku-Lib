package band.kessoku.lib.platform.impl;

import band.kessoku.lib.base.ModUtils;
import band.kessoku.lib.platform.api.ModLoader;

public class KessokuPlatformServices {
    private static final ModLoader modLoader = ModUtils.loadService(ModLoader.class);

    public static ModLoader getModLoader() {
        return modLoader;
    }
}
