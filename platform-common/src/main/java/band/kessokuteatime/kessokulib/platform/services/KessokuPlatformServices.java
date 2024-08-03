package band.kessokuteatime.kessokulib.platform.services;

import band.kessokuteatime.kessokulib.base.ModUtils;
import band.kessokuteatime.kessokulib.platform.api.ModLoader;

public class KessokuPlatformServices {
    private static final ModLoader modLoader = ModUtils.load(ModLoader.class);

    public static ModLoader getModLoader() {
        return modLoader;
    }
}
