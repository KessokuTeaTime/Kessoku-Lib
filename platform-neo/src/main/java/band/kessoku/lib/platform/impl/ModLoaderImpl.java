package band.kessoku.lib.platform.impl;

import band.kessoku.lib.platform.api.Env;
import band.kessoku.lib.platform.api.ModData;
import band.kessoku.lib.platform.api.ModLoader;
import com.google.auto.service.AutoService;
import net.minecraft.MinecraftVersion;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforgespi.language.IModInfo;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
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

    @Override
    public Path getGameFolder() {
        return FMLPaths.GAMEDIR.get();
    }

    @Override
    public Path getConfigFolder() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public Path getModsFolder() {
        return FMLPaths.MODSDIR.get();
    }

    @Override
    public boolean isModLoaded(String id) {
        return ModList.get().isLoaded(id);
    }

    @Override
    public Collection<String> getModIds() {
        return ModList.get().getMods().stream().map(IModInfo::getModId).toList();
    }

    @Override
    public Collection<? extends ModData> getMods() {
        return ModList.get().getMods().stream().map(ModDataImpl::new).toList();
    }
}
