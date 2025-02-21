/*
 * Copyright (c) 2024, 2025 KessokuTeaTime
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
package band.kessoku.lib.api.config.serializer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import band.kessoku.lib.api.base.reflect.ModifiersUtil;
import band.kessoku.lib.api.base.reflect.ReflectUtil;
import band.kessoku.lib.api.config.*;
import band.kessoku.lib.api.config.exception.IllegalValueException;
import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.electronwill.nightconfig.toml.TomlParser;
import com.electronwill.nightconfig.toml.TomlWriter;
import org.apache.commons.io.output.StringBuilderWriter;

/**
 * @author AmarokIce
 */
public class TomlConfigSerializer implements ConfigSerializer {
    public static final TomlConfigSerializer INSTANCE = new TomlConfigSerializer();

    private TomlConfigSerializer() {}

    @Override
    public void serializer(final String valueStr, final Class<?> clazz) throws IllegalValueException {
        final TomlParser tomlParser = new TomlParser();
        readFromConfig(tomlParser.parse(valueStr), clazz);
    }

    static void readFromConfig(final CommentedConfig config, final Class<?> clazz) {
        for (final Field field : clazz.getDeclaredFields()) {
            if (!ModifiersUtil.isStatic(field) || Modifier.isFinal(field.getModifiers())) {
                continue;
            }

            ReflectUtil.markAccessible(field);

            final String name = field.isAnnotationPresent(Name.class)
                    ? field.getAnnotation(Name.class).value()
                    : field.getName();

            if (!config.contains(name)) {
                continue;
            }

            if (field.getType().isAnnotationPresent(Config.class)) {
                readFromConfig(config.get(name), field.getType());
                continue;
            }

            try {
                field.set(null, config.get(name));
            } catch (IllegalAccessException ignored) {
            }
        }
    }

    @Override
    public String deserializer(final Class<?> clazz) {
        final CommentedConfig config = TomlFormat.newConfig();
        writeToConfig(config, clazz);

        final TomlWriter writer = new TomlWriter();
        final StringBuilderWriter stringWriter = new StringBuilderWriter();
        writer.write(config, stringWriter);

        return stringWriter.toString();
    }

    static void writeToConfig(final CommentedConfig config, final Class<?> clazz) {
        for (final Field field : clazz.getDeclaredFields()) {
            if (!ModifiersUtil.isStatic(field) || Modifier.isFinal(field.getModifiers())) {
                continue;
            }

            final String name = field.isAnnotationPresent(Name.class)
                    ? field.getAnnotation(Name.class).value()
                    : field.getName();

            final Comment[] comments = field.isAnnotationPresent(Comments.class)
                    ? field.getAnnotation(Comments.class).value()
                    : new Comment[] {};

            for (final Comment comment : comments) {
                config.setComment(name, comment.value());
            }

            if (field.getType().isAnnotationPresent(Config.class)) {
                final CommentedConfig temp = TomlFormat.newConfig();
                writeToConfig(temp, field.getType());
                config.add(name, temp);
                continue;
            }

            try {
                config.add(name, field.get(null));
            } catch (IllegalAccessException ignored) {
            }
        }
    }
}
