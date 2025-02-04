/*
 * Copyright (c) 2024 KessokuTeaTime
 *
 * Licensed under the GNU Lesser General Pubic License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package band.kessoku.lib.api.config.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import band.kessoku.lib.api.KessokuLib;
import band.kessoku.lib.api.config.Config;
import band.kessoku.lib.api.config.ConfigSerializer;
import band.kessoku.lib.api.config.serializer.ConfigSerializers;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * The config handler, also see {@link Config @Config}. <br>
 * The config format or file type determined by config codec.
 *
 * @see Config @Config
 * @see ConfigSerializer ConfigSerializer
 * @see ConfigSerializers Basic ConfigSerializer
 *
 * @author AmarokIce
 */
public final class ConfigHandler {
    private static Path configDir;

    /**
     * All mods config(s).
     */
    private static final Table<String, String, ConfigHandler> MOD_CONFIG_DATA = HashBasedTable.create();


    private final String path;
    private final Class<?> configClazz;
    private final ConfigSerializer serializer;

    private ConfigHandler(String path, Class<?> configClazz, ConfigSerializer configSerializer) {
        this.path = path;
        this.configClazz = configClazz;
        this.serializer = configSerializer;
    }


    private static void registerConfig(final Class<?> config) {
        final Config configAnno = config.getAnnotation(Config.class);
        final String modid = configAnno.modid();
        String configName = configAnno.name();
        final ConfigSerializer configSerializer = ConfigSerializers.getSerializer(configAnno.serialize());

        if (configName.isEmpty()) {
            configName = modid;
        }

        if (Objects.isNull(configSerializer)) {
            throw new IllegalArgumentException("Config serializer not found.");
        }

        MOD_CONFIG_DATA.put(modid, configName, new ConfigHandler(modid, config, configSerializer));

        Path configPath = ConfigHandler.configDir.resolve(configName);
        try {
            configSerializer.serializer(Files.readString(configPath), config);
            String data = configSerializer.deserializer(config);
            Files.write(configPath, data.getBytes());
        } catch (IOException e) {
            KessokuLib.getLogger().error("Can't read config file: " + configName, e);
        }
    }

    public static void handleConfigs(List<Class<?>> list, Path configDir) {
        ConfigHandler.configDir = configDir;
        list.forEach(ConfigHandler::registerConfig);
    }
}
