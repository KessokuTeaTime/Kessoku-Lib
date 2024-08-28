package band.kessoku.lib.entrypoint;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import band.kessoku.lib.base.ModUtils;
import band.kessoku.lib.entrypoint.api.KessokuClientModInitializer;
import band.kessoku.lib.entrypoint.api.KessokuDedicatedServerModInitializer;
import band.kessoku.lib.entrypoint.api.KessokuModInitializer;
import com.google.common.collect.ImmutableMap;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class KessokuEntrypointEntrypoint implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer, PreLaunchEntrypoint {
    private static ImmutableMap<String,KessokuInfo> modInfoList;

    @Override
    public void onInitialize() {
        ModUtils.getLogger().info(KessokuEntrypoint.MARKER, "KessokuLib-Entrypoint is loaded!");
        FabricLoader.getInstance().invokeEntrypoints("kMain", KessokuModInitializer.class, KessokuModInitializer::onInitialize);
    }

    @Override
    public void onInitializeClient() {
        FabricLoader.getInstance().invokeEntrypoints("kClient", KessokuClientModInitializer.class, KessokuClientModInitializer::onInitializeClient);
    }

    @Override
    public void onInitializeServer() {
        FabricLoader.getInstance().invokeEntrypoints("kServer", KessokuDedicatedServerModInitializer.class, KessokuDedicatedServerModInitializer::onInitializeServer);
    }

    @Override
    public void onPreLaunch() {
        for (ModContainer modContainer : FabricLoader.getInstance().getAllMods()) {
            Path path = modContainer.findPath("kessoku.json").orElse(null);
            if (path == null) continue;

            try {
                String json = Files.readString(path);
            } catch (Exception e) {
                throw new RuntimeException("Failed to read accessWidener file from mod " + modContainer.getMetadata().getId(), e);
            }
        }
    }

    public record KessokuInfo(Map<String, EntrypointInfo<?>> entrypointMap) {
    }

    public record EntrypointInfo<T>(Class<T> entrypoint, T instance) {
        public static <K> EntrypointInfo<K> create(Class<K> entrypoint) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            return new EntrypointInfo<>(entrypoint, entrypoint.getDeclaredConstructor().newInstance());
        }
    }
}
