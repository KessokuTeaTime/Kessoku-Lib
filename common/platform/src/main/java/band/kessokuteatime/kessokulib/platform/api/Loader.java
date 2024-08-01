package band.kessokuteatime.kessokulib.platform.api;

import band.kessokuteatime.kessokulib.base.ModUtils;
import band.kessokuteatime.kessokulib.platform.services.LoaderService;

public class Loader {
    private static LoaderService service;

    public static boolean isFabric() {
        return Loader.service.isFabric();
    }

    public static boolean isNeoForge() {
        return Loader.service.isNeoForge();
    }

    public static ModInfo getModInfo() {
        return Loader.service.getModInfo();
    }

    public static Env getEnv() {
        return Loader.service.getEnv();
    }

    static void loadService() {
        if (service != null) throw new IllegalStateException("LoaderService is already loaded!");
        service = ModUtils.load(LoaderService.class);
    }
}
