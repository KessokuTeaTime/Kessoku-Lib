package band.kessoku.lib.platform.api;

import band.kessoku.lib.platform.impl.KessokuPlatformServices;

public interface ModLoader {
    static ModLoader getInstance() {
        return KessokuPlatformServices.getModLoader();
    }

    ModData getModData(String modid);
    boolean isFabric();
    boolean isNeoForge();
    Env getEnv();
}
