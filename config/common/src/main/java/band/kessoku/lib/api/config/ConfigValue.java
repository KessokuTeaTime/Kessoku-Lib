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

import band.kessoku.lib.api.base.reflect.ReflectUtil;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public interface ConfigValue<F, T> extends Supplier<F> {
    void setFrom(F value);

    void setTo(T value);

    F getFrom();

    T getTo();

    @Override
    default F get() {
        return this.getFrom();
    }

    void reset();

    Type getType();

    F getDefaultFrom();

    T getDefaultTo();

    enum Type {
        ARRAY(List.class),
        MAP(Map.class),
        BOOLEAN(Boolean.class),
        STRING(String.class),
        INTEGER(Character.class, Byte.class, Short.class, Integer.class, Long.class),
        DECIMAL(Float.class, Double.class),
        NULL(Void.class);

        public final Class<?>[] classes;

        Type(Class<?>... classes) {
            this.classes = classes;
        }

        public static Type asType(Object o) {
            if (o.getClass().isArray()) return ARRAY;
            for (Type type : Type.values()) {
                if (type.is(o)) return type;
            }
            return NULL;
        }

        public boolean canCastFrom(Type type) {
            // todo
            return this == type;
        }

        public boolean is(Object o) {
            return ReflectUtil.isAssignableFrom(o, this.classes);
        }

        public Object getDefaultValue() {
            return switch (this) {
                case ARRAY -> new Object[0];
                case MAP -> Map.of();
                case BOOLEAN -> false;
                case STRING -> "";
                case INTEGER -> 0L;
                case DECIMAL -> 0.0d;

                default -> null;
            };
        }
    }
}
