package band.kessoku.lib.platform.api;

import band.kessoku.lib.platform.impl.KessokuPlatformServices;

import java.nio.file.Path;

public interface ModLoader {
    static ModLoader getInstance() {
        return KessokuPlatformServices.getModLoader();
    }

    ModData getModData(String modid);
    boolean isFabric();
    boolean isNeoForge();
    Env getEnv();
    Path getGameFolder();
    Path getConfigFolder();
    Path getModsFolder();
    boolean isModLoaded(String id);
}
