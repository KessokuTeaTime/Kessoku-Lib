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
package band.kessoku.lib.api.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import band.kessoku.lib.api.KessokuLib;
import band.kessoku.lib.api.base.reflect.ModifiersUtil;
import band.kessoku.lib.impl.config.AbstractConfig;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.core.util.ReflectionUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public final class KessokuConfig {
    private static final Map<AbstractConfig, ConfigSerializer> configs = new HashMap<>();
    private static final Map<Class<? extends ConfigSerializer>, ConfigSerializer> serializerCache = new HashMap<>();

    public static final String MOD_ID = "kessoku_config";
    public static final String NAME = "Kessoku Config API";
    public static final Marker MARKER = MarkerFactory.getMarker("[" + NAME + "]");

    private static final Map<AbstractConfig, List<Consumer<AbstractConfig>>> preSave = new HashMap<>();
    private static final Map<AbstractConfig, List<BiConsumer<AbstractConfig, Boolean>>> postSave = new HashMap<>();
    private static final Map<AbstractConfig, List<Consumer<AbstractConfig>>> preLoad = new HashMap<>();
    private static final Map<AbstractConfig, List<BiConsumer<AbstractConfig, Boolean>>> postLoad = new HashMap<>();

    @SuppressWarnings("unused")
    public static <T extends AbstractConfig, S extends ConfigSerializer> T register(Class<T> configClass, Class<S> serializer) {
        Objects.requireNonNull(serializer, "Serializer shouldn't be null!");
        Objects.requireNonNull(configClass, "Config shouldn't be null!");

        // Create the config instance
        T config = ReflectionUtil.instantiate(configClass);

        // Create config file
        try {
            FileUtils.touch(config.getPath().toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        configs.put(config, getSerializerInstance(serializer));
        return config;
    }

    public static boolean save(AbstractConfig config) {
        // Pre save
        preSave.getOrDefault(config, List.of()).forEach(consumer -> consumer.accept(config));
        File configFile = config.getPath().toFile();
        ConfigSerializer serializer = config.getSerializer();

        boolean success = true;
        try (FileWriter writer = new FileWriter(configFile, StandardCharsets.UTF_8)) {
            writer.write(serializer.serialize(config.serialize()));
        } catch (IOException e) {
            success = false;
        }

        // post save
        final boolean finalResult = success;
        postSave.getOrDefault(config, List.of()).forEach(biConsumer -> biConsumer.accept(config, finalResult));
        return success;
    }

    public static boolean load(AbstractConfig config) {
        // pre load
        preLoad.getOrDefault(config, List.of()).forEach(consumer -> consumer.accept(config));
        ConfigSerializer serializer = config.getSerializer();
        File file = config.getPath().toFile();
        if (!file.exists()) {
            config.save();
            return false;
        }

        boolean success = true;
        try {
            Map<String, Object> map = serializer.deserialize(FileUtils.readFileToString(file, StandardCharsets.UTF_8));
            // Put values into the config
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object loadedValue = entry.getValue();

                ConfigValue configValue;
                // Check the value is public and not static
                try {
                    Field field = config.getClass().getField(key);
                    if (!ModifiersUtil.isPublicOrStatic(field, true, false)) {
                        continue;
                    }
                    if (!field.getDeclaringClass().isAssignableFrom(ConfigValue.class)) {
                        continue;
                    }
                    configValue = (ConfigValue) ReflectionUtil.getFieldValue(field, config);
                } catch (NoSuchFieldException e) {
                    // not found
                    continue;
                }

                ConfigValue.Type type = ConfigValue.Type.asType(loadedValue);
                // Check if the type is valid to deserialize
                if (type == ConfigValue.Type.NULL) {
                    KessokuLib.getLogger().error(KessokuConfig.MARKER, "Illegal type`{}` found in the file!", loadedValue.getClass().getName());
                    continue;
                }

                // Check if the type matches the value's type
                if (configValue.getType() != type) {
                    KessokuLib.getLogger().error(KessokuConfig.MARKER, "Illegal type`{}` found in the file! Expect {}.", type.toString().toLowerCase(), configValue.getType().toString().toLowerCase());
                    continue;
                }

                configValue.setTo(loadedValue);
            }
        } catch (IOException e) {
            success = false;
        }

        final boolean finalResult = success;
        postLoad.getOrDefault(config, List.of()).forEach(biConsumer -> biConsumer.accept(config, finalResult));
        return success;
    }

    public static void registerPreSaveListener(final AbstractConfig config, Consumer<AbstractConfig> preSaveConsumer) {
        Objects.requireNonNull(preSave.putIfAbsent(config, new ArrayList<>())).add(preSaveConsumer);
    }

    public static void registerPreLoadListener(AbstractConfig config, Consumer<AbstractConfig> preLoadConsumer) {
        Objects.requireNonNull(preLoad.putIfAbsent(config, new ArrayList<>())).add(preLoadConsumer);
    }

    public static void registerPostSaveListener(AbstractConfig config, BiConsumer<AbstractConfig, Boolean> postSaveConsumer) {
        Objects.requireNonNull(postSave.putIfAbsent(config, new ArrayList<>())).add(postSaveConsumer);
    }

    public static void registerPostLoadListener(AbstractConfig config, BiConsumer<AbstractConfig, Boolean> postLoadConsumer) {
        Objects.requireNonNull(postLoad.putIfAbsent(config, new ArrayList<>())).add(postLoadConsumer);
    }

    // Serializers
    @Contract(pure = true)
    @Nullable
    public static <T extends AbstractConfig> ConfigSerializer getSerializer(T config) {
        return configs.get(config);
    }

    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    @NotNull
    private static <T extends ConfigSerializer> T getSerializerInstance(Class<T> serializer) {
        try {
            return (T) Objects.requireNonNull(serializerCache.containsKey(serializer)
                    ? serializerCache.get(serializer)
                    : serializerCache.put(serializer,
                    serializer.getConstructor().newInstance()));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
