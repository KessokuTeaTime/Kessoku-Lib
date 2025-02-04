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
import band.kessoku.lib.api.config.*;
import band.kessoku.lib.api.config.exception.IllegalValueException;
import club.someoneice.json.JSON;
import club.someoneice.json.node.JsonNode;
import club.someoneice.json.node.MapNode;
import club.someoneice.json.processor.Json5Builder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author AmarokIce
 */
public final class Json5ConfigSerializer implements ConfigSerializer {
    public static final Json5ConfigSerializer INSTANCE = new Json5ConfigSerializer();

    private Json5ConfigSerializer() {}

    @Override
    public void serializer(final String valueStr, final Class<?> clazz) throws IllegalValueException {
        final MapNode node = JSON.json5.parse(valueStr).asMapNodeOrEmpty();
        JsonConfigSerializer.readFromJsonNode(node, clazz);
    }

    @Override
    public String deserializer(final Class<?> clazz) {
        final Json5Builder builder = new Json5Builder();
        final Json5Builder.ObjectBean bean = builder.getObjectBean();
        writeToJsonNode(builder, bean, clazz);
        builder.put(bean);
        return builder.build();
    }

    static void writeToJsonNode(final Json5Builder builder, final Json5Builder.ObjectBean bean, final Class<?> clazz) {
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
                bean.addNote(comment.value());
            }

            if (field.getType().isAnnotationPresent(Config.class)) {
                final Json5Builder.ObjectBean temp = builder.getObjectBean();
                writeToJsonNode(builder, temp, field.getType());
                bean.addBean(name, temp);
                continue;
            }

            try {
                bean.put(name, new JsonNode<>(field.get(null)));
            } catch (IllegalAccessException ignored) {
            }
        }
    }
}
