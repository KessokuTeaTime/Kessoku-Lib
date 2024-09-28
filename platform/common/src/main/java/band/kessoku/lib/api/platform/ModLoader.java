package band.kessoku.lib.api.platform;

import band.kessoku.lib.services.platform.ModLoaderService;

import java.nio.file.Path;
import java.util.Collection;

public final class ModLoader {
    private ModLoader() {
    }

    public static ModData getModData(String modid) {
        return ModLoaderService.getInstance().getModData(modid);
    }

    public static boolean isFabric() {
        return ModLoaderService.getInstance().isFabric();
    }

    public static boolean isNeoForge() {
        return ModLoaderService.getInstance().isNeoForge();
    }

    public static Env getEnv() {
        return ModLoaderService.getInstance().getEnv();
    }

    public static Path getGameFolder() {
        return ModLoaderService.getInstance().getGameFolder();
    }

    public static Path getConfigFolder() {
        return ModLoaderService.getInstance().getConfigFolder();
    }

    public static Path getModsFolder() {
        return ModLoaderService.getInstance().getModsFolder();
    }

    public static boolean isModLoaded(String id) {
        return ModLoaderService.getInstance().isModLoaded(id);
    }

    public static Collection<String> getModIds() {
        return ModLoaderService.getInstance().getModIds();
    }

    public static Collection<? extends ModData> getMods() {
        return ModLoaderService.getInstance().getMods();
    }

    public static boolean isDevEnv() {
        return ModLoaderService.getInstance().isDevEnv();
    }
}
