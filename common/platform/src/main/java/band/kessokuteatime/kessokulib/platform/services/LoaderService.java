package band.kessokuteatime.kessokulib.platform.services;

import band.kessokuteatime.kessokulib.platform.api.Env;
import band.kessokuteatime.kessokulib.platform.api.ModInfo;

public interface LoaderService {
    boolean isFabric();
    boolean isNeoForge();
    ModInfo getModInfo();
    Env getEnv();
}
