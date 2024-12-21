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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import band.kessoku.lib.api.base.reflect.ReflectUtil;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

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
        CFG_VALUE(ConfigValue.class);//,
        //NULL(Void.class);

        public final Class<?>[] classes;

        Type(final Class<?>... classes) {
            this.classes = classes;
        }

        public static Optional<Type> asType(Object o) {
            if (o == null) return Optional.empty();
            if (o.getClass().isArray()) return Optional.of(ARRAY);
            for (final Type type : Type.values()) {
                if (type.is(o)) return Optional.of(type);
            }
            return Optional.empty();
        }

        // Just like how java behave
        public boolean canCastFrom(final Type type) {
            return false;
        }

        // Just like how java behave
        public Object cast(final Object o) {
            if (o == null) return null;
            return switch (this) {
                case ARRAY -> {
                    if (o.getClass().isArray()) {
                        yield o;
                    } else {
                        if (o.getClass().isAssignableFrom(Iterable.class)) {
                            yield Lists.newArrayList(o);
                        }
                        throw new ClassCastException();
                    }
                };
                case MAP -> (Map<?, ?>) o;
                case BOOLEAN -> (boolean) o;
                case STRING -> (String) o;
                case INTEGER -> (long) o;
                case DECIMAL -> (double) o;
                case CFG_VALUE -> o;

                //default -> null;
            };
        }

        public boolean compatible(final Object o) {
            final Optional<Type> optional = asType(o);
            return optional.filter(this::canCastFrom).isPresent();
        }

        public boolean is(final Object o) {
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

                //default -> null;
            };
        }
    }
}
