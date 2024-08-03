package band.kessoku.lib.platform.impl;

import band.kessoku.lib.platform.api.Env;
import band.kessoku.lib.platform.api.ModData;
import band.kessoku.lib.platform.api.ModLoader;
import com.google.auto.service.AutoService;
import net.neoforged.fml.loading.FMLLoader;

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
        return false;
    }

    @Override
    public boolean isNeoForge() {
        return true;
    }

    @Override
    public Env getEnv() {
        return FMLLoader.getDist().isClient() ? Env.CLIENT : Env.SERVER;
    }
}