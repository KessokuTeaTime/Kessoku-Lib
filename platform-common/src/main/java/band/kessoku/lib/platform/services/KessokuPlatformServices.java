package band.kessoku.lib.platform.services;

import band.kessoku.lib.platform.api.ModLoader;
import band.kessokuteatime.kessokulib.base.ModUtils;

public class KessokuPlatformServices {
    private static final ModLoader modLoader = ModUtils.load(ModLoader.class);

    public static ModLoader getModLoader() {
        return modLoader;
    }
}
