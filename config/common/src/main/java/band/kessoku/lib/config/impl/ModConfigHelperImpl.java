package band.kessoku.lib.config.impl;

import band.kessoku.lib.base.ModUtils;
import band.kessoku.lib.config.api.serializers.night.HoconSerializer;
import band.kessoku.lib.config.api.serializers.night.JsonSerializer;
import band.kessoku.lib.config.api.serializers.night.YamlSerializer;
import band.kessoku.lib.config.api.serializers.quilt.Json5Serializer;
import band.kessoku.lib.config.api.serializers.night.TomlSerializer;
import band.kessoku.lib.platform.api.ModLoader;

import org.quiltmc.config.api.Serializer;
import org.quiltmc.config.implementor_api.ConfigEnvironment;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ModConfigHelperImpl {
    private static ConfigEnvironment ENV;
    private static final Path CONFIG_DIR = ModLoader.getInstance().getConfigFolder();

    public static void init() {
        Map<String, Serializer> serializerMap = new LinkedHashMap<>();

        serializerMap.put("json5", Json5Serializer.INSTANCE);

        serializerMap.put("toml", TomlSerializer.INSTANCE);
        serializerMap.put("json", JsonSerializer.INSTANCE);
        serializerMap.put("yaml", YamlSerializer.INSTANCE);
        serializerMap.put("hocon", HoconSerializer.INSTANCE);

        String globalConfigExtension = System.getProperty("kessoku.config.globalConfigExtension");
        String defaultConfigExtension = System.getProperty("kessoku.config.defaultConfigExtension");

        Serializer[] serializers = serializerMap.values().toArray(new Serializer[0]);

        if (globalConfigExtension != null && !serializerMap.containsKey(globalConfigExtension)) {
            throw new RuntimeException("Cannot use file extension " + globalConfigExtension + " globally: no matching serializer found");
        }

        if (defaultConfigExtension != null && !serializerMap.containsKey(defaultConfigExtension)) {
            throw new RuntimeException("Cannot use file extension " + defaultConfigExtension + " by default: no matching serializer found");
        }

        if (defaultConfigExtension == null) {
            ENV = new ConfigEnvironment(CONFIG_DIR, globalConfigExtension, serializers[0]);

            for (int i = 1; i < serializers.length; ++i) {
                ENV.registerSerializer(serializers[i]);
            }
        } else {
            ENV = new ConfigEnvironment(CONFIG_DIR, globalConfigExtension, serializerMap.get(defaultConfigExtension));

            for (Serializer serializer : serializers) {
                ENV.registerSerializer(serializer);
            }
        }
    }

    public static ConfigEnvironment getConfigEnvironment() {
        return ENV;
    }

    private ModConfigHelperImpl() {

    }
}
