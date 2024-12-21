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
package band.kessoku.lib.api.config.values;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import band.kessoku.lib.api.config.ConfigValue;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class MapValue<K extends ConfigValue<?, String>, V extends ConfigValue<?, F>, F> extends HashMap<K, V> implements ConfigValue<Map<K, V>, Map<K, F>> {

    @Override
    public void setFrom(final Map<K, V> value) {
        this.putAll(value);
    }

    @Override
    public void setTo(Map<K, F> value) {
    }

    @Override
    public Map<K, V> getFrom() {
        return Map.of();
    }

    @Override
    public Map<String, F> getTo() {
        return Map.of();
    }

    @Override
    public void reset() {

    }

    @Override
    public Type getType() {
        return Type.MAP;
    }

    @Override
    public Map<K, V> getDefaultFrom() {
        return Map.of();
    }

    @Override
    public Map<String, F> getDefaultTo() {
        return Map.of();
    }

    // constructor
    @NotNull
    public static <K extends ConfigValue<?, String>, V extends ConfigValue<?, ?>> MapValue<K, V> of() {
        return new MapValue<>(HashMap::new);
    }

    @Contract("_ -> new")
    @NotNull
    public static <K extends ConfigValue<?, String>, V extends ConfigValue<?, ?>> MapValue<K, V> of(final Map<K, V> m) {
        return new MapValue<>(() -> m);
    }

    @Contract("_ -> new")
    @NotNull
    public static <K extends ConfigValue<?, String>, V extends ConfigValue<?, ?>> MapValue<K, V> of(final Supplier<Map<K, V>> mapSupplier) {
        return new MapValue<>(mapSupplier);
    }

    public Map<String, ?> normalize() {
        Map<String, Object> map = new HashMap<>();
        this.forEach((key, value) -> {
            if (List.of(Type.ARRAY, Type.MAP).contains(value.getType())) {
                try {
                    map.put(key.getTo(), MethodUtils.getAccessibleMethod(value.getClass(), "normalize").invoke(value));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            } else {
                map.put(key.getTo(), value.getTo());
            }
        });
        return map;
    }

    public static <V extends ConfigValue<E, ?>, E> Map<StringValue, V> transform(Map<String, V> map, Function<E, V> valueBoxFunc) {
        final Map<StringValue, V> result = new HashMap<>();
        return result;
    }
}
