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
package band.kessoku.lib.api.config.serializer;

import band.kessoku.lib.api.base.reflect.ModifiersUtil;
import band.kessoku.lib.api.base.reflect.ReflectUtil;
import band.kessoku.lib.api.config.Config;
import band.kessoku.lib.api.config.ConfigSerializer;
import band.kessoku.lib.api.config.Name;
import band.kessoku.lib.api.config.exception.IllegalValueException;
import club.someoneice.json.JSON;
import club.someoneice.json.node.JsonNode;
import club.someoneice.json.node.MapNode;
import club.someoneice.json.processor.JsonBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author AmarokIce
 */
public final class JsonConfigSerializer implements ConfigSerializer {
    public static final JsonConfigSerializer INSTANCE = new JsonConfigSerializer();

    private JsonConfigSerializer() {}

    @Override
    public void serializer(final String valueStr, final Class<?> clazz) throws IllegalValueException {
        MapNode node = JSON.json.parse(valueStr).asMapNodeOrEmpty();
        readFromJsonNode(node, clazz);
    }

    static void readFromJsonNode(final MapNode node, final Class<?> clazz) {
        for (final Field field : clazz.getDeclaredFields()) {
            if (!ModifiersUtil.isStatic(field) || Modifier.isFinal(field.getModifiers())) {
                continue;
            }

            ReflectUtil.markAccessible(field);

            final String name = field.isAnnotationPresent(Name.class)
                    ? field.getAnnotation(Name.class).value()
                    : field.getName();

            if (!node.has(name)) {
                continue;
            }

            if (field.getType().isAnnotationPresent(Config.class)) {
                readFromJsonNode(node.get(name).asMapNodeOrEmpty(), field.getType());
                continue;
            }

            try {
                field.set(null, node.get(name).getObj());
            } catch (IllegalAccessException ignored) {
            }
        }
    }

    @Override
    public String deserializer(final Class<?> clazz) {
        final MapNode node = new MapNode();
        writeToJsonNode(node, clazz);
        return JsonBuilder.prettyPrint(node);
    }

    static void writeToJsonNode(final MapNode node, final Class<?> clazz) {
        for (final Field field : clazz.getDeclaredFields()) {
            if (!ModifiersUtil.isStatic(field) || Modifier.isFinal(field.getModifiers())) {
                continue;
            }

            final String name = field.isAnnotationPresent(Name.class)
                    ? field.getAnnotation(Name.class).value()
                    : field.getName();

            if (field.getType().isAnnotationPresent(Config.class)) {
                MapNode temp = new MapNode();
                writeToJsonNode(temp, field.getType());
                node.put(name, temp);
                continue;
            }

            try {
                node.put(name, new JsonNode<>(field.get(null)));
            } catch (IllegalAccessException ignored) {
            }
        }
    }
}
