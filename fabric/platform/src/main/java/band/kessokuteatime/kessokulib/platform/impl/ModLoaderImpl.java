package band.kessokuteatime.kessokulib.platform.impl;

import band.kessokuteatime.kessokulib.platform.api.Env;
import band.kessokuteatime.kessokulib.platform.api.ModData;
import band.kessokuteatime.kessokulib.platform.api.ModLoader;
import com.google.auto.service.AutoService;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@AutoService(ModLoader.class)
public class ModLoaderImpl implements ModLoader {
    private final Map<String, ModData> modDataMap = new ConcurrentHashMap<>();

    @Override
    public ModData getModData(String modid) {
        return modDataMap.computeIfAbsent(modid, ModDataImpl::new);
    }

    @Override
    public boolean isFabric() {
        return true;
    }

    @Override
    public boolean isNeoForge() {
        return false;
    }

    @Override
    public Env getEnv() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? Env.CLIENT : Env.SERVER;
    }
}
