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
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.*;

import band.kessoku.lib.api.KessokuLib;
import band.kessoku.lib.api.base.reflect.ModifiersUtil;
import band.kessoku.lib.api.base.reflect.ReflectUtil;
import band.kessoku.lib.api.config.api.*;
import band.kessoku.lib.api.config.exception.IllegalValueException;
import club.someoneice.json.Pair;
import com.google.common.collect.*;
import com.google.common.io.Files;

/**
 * The config handler, also see {@link Config Config}. <br>
 * The config format or file type determined by config codec.
 *
 * @see Config Config
 * @see Codec Codec
 * @see ConfigBasicCodec Basic Config Codec
 *
 * @author AmarokIce
 */
public final class ConfigHandler {
    private static Path configDir;
    /**
     * All mods config(s).
     */
    private static final Table<String, String, Pair<Class<?>, ConfigHandler>> MOD_CONFIG_DATA = HashBasedTable.create();

    /**
     * Record all config fields, next time to use needn't scan it again.
     *
     * @see ConfigHandler#readByClass(Class, ConfigHandler)
     * @see ConfigHandler#saveToClass(Class, ConfigHandler, Map)
     */
    private ImmutableList<Field> fields;

    /**
     * Format codec for {@code Config} and {@code Category}. <br>
     *
     * @see Category
     */
    private final Codec<Map<String, ConfigData>> formatCodec;
    private final Class<?> clazz;

    private ConfigHandler(Codec<Map<String, ConfigData>> codec, Class<?> clazz) {
        this.formatCodec = codec;
        this.clazz = clazz;
    }

    private static void registerConfig(Class<?> clazz) throws IllegalAccessException, IOException {
        Config configSetting = clazz.getAnnotation(Config.class);
        final String modid = configSetting.value();
        final String name = configSetting.name().isEmpty() ? configSetting.value() : configSetting.name();
        final var codec = ConfigBasicCodec.getCodec(configSetting.codec());

        ConfigHandler configHandler = new ConfigHandler(codec, clazz);
        MOD_CONFIG_DATA.put(modid, name, new Pair<>(clazz, configHandler));

        var defMap = readByClass(clazz, configHandler);

        File cfgFile = ConfigHandler.configDir.resolve(name).toFile();
        if (!cfgFile.exists()) {
            cfgFile.createNewFile();
        }

        String data = Arrays.toString(Files.toByteArray(cfgFile));
        var localMap = codec.encode(data);

        Map<String, ConfigData> commands = Maps.newLinkedHashMap();
        for (Map.Entry<String, ConfigData> entry : defMap.entrySet()) {
            String key = entry.getKey();
            commands.put(key, localMap.containsKey(key) ? localMap.get(key) : entry.getValue());
        }

        saveToClass(clazz, configHandler, commands);
        data = codec.decode(commands);
        Files.write(data.getBytes(), cfgFile);
    }

    public static Map<String, ConfigData> readByClass(Class<?> clazz, ConfigHandler cfg) throws IllegalAccessException {
        Map<String, ConfigData> map = Maps.newLinkedHashMap();

        if (Objects.isNull(cfg.fields)) {
            scanFields(clazz, cfg);
        }

        for (Field field : cfg.fields) {
            String name = field.isAnnotationPresent(Name.class) ? field.getAnnotation(Name.class).value() : field.getName();
            List<String> comments = field.isAnnotationPresent(Comment.class) ? Lists.newArrayList(field.getAnnotation(Comment.class).value()) : Collections.emptyList();

            String data = ReflectUtil.isAssignableFrom(field, Category.class)
                    ? cfg.formatCodec.decode(readByClass(field.getType(), cfg))
                    : ((ConfigValue<?>) field.get(null)).decode();

            map.put(name, new ConfigData(name, data, comments));
        }

        return map;
    }

    public static void saveToClass(Class<?> clazz, ConfigHandler cfg, Map<String, ConfigData> data) throws IllegalAccessException {
        if (Objects.isNull(cfg.fields)) {
            scanFields(clazz, cfg);
        }

        for (Field field : cfg.fields) {
            String name = field.isAnnotationPresent(Name.class) ? field.getAnnotation(Name.class).value() : field.getName();
            var raw = ((ConfigValue<?>) field.get(null));
            if (!ReflectUtil.isAssignableFrom(field, Category.class)) {
                try {
                    raw.encode(data.get(name).rawValue());
                } catch (IllegalValueException e) {
                    KessokuLib.getLogger().error(e.getMessage(), e);
                    data.remove(name);
                }

                continue;
            }

            saveToClass(field.getType(), cfg, cfg.formatCodec.encode(data.get(name).rawValue()));
        }
    }

    public static void scanFields(Class<?> clazz, ConfigHandler cfg) {
        ImmutableList.Builder<Field> builder = ImmutableList.builder();
        for (Field field : clazz.getDeclaredFields()) {
            if (!ModifiersUtil.isStatic(field)) {
                continue;
            }

            if (!ReflectUtil.isAssignableFrom(field, ConfigValue.class)) {
                continue;
            }

            ReflectUtil.markAccessible(field);
            builder.add(field);
        }

        cfg.fields = builder.build();
    }

    /**
     * The magic method for auto scan all config class with {@link Config @Config}.
     *
     * @see Config @Config
     * @param configDir The based config directory, get by loader.
     */
    public static void handleConfigs(List<Class<?>> list, Path configDir) {
        ConfigHandler.configDir = configDir;
        list.forEach(it -> {
           try {
               registerConfig(it);
           } catch (Exception e) {
               KessokuLib.getLogger().error(e.getMessage(), e);
           }
        });
    }
}
