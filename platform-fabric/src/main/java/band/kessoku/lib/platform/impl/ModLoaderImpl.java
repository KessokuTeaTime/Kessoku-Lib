package band.kessoku.lib.platform.impl;

import band.kessoku.lib.platform.api.Env;
import band.kessoku.lib.platform.api.ModData;
import band.kessoku.lib.platform.api.ModLoader;
import com.google.auto.service.AutoService;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;
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

    @Override
    public Path getGameFolder() {
        return FabricLoader.getInstance().getGameDir();
    }

    @Override
    public Path getConfigFolder() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public Path getModsFolder() {
        return getGameFolder().resolve("mods");
    }

    @Override
    public boolean isModLoaded(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }
}
