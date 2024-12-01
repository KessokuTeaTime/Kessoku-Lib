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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import band.kessoku.lib.api.config.ConfigValue;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class MapValue<K extends ConfigValue<?, String>, V extends ConfigValue<?, ?>> extends DefaultConfigValue<Map<K, V>> implements Map<K, V> {
    private MapValue(final Supplier<Map<K, V>> defaultValue) {
        super(defaultValue);
    }

    @Override
    public Type getType() {
        return Type.MAP;
    }

    @Override
    public void reset() {
        this.value = new HashMap<>(this.defaultValue.get());
    }

    @Override
    @NotNull
    @Unmodifiable
    public Map<K, V> getDefaultFrom() {
        return Collections.unmodifiableMap(super.getDefaultFrom());
    }

    @Override
    @NotNull
    @Unmodifiable
    public Map<K, V> getDefaultTo() {
        return Collections.unmodifiableMap(super.getDefaultTo());
    }

    public Map<String, ?> normalize() {
        HashMap map = new HashMap<>();
        this.value.forEach((key, value) -> {
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

    // Map
    @Override
    public int size() {
        return this.value.size();
    }

    @Override
    public boolean isEmpty() {
        return this.value.isEmpty();
    }

    @Override
    public boolean containsKey(final Object key) {
        return this.value.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return this.value.containsValue(value);
    }

    @Override
    public V get(final Object key) {
        return this.value.get(key);
    }

    @Override
    public V put(final K key, final V value) {
        return this.value.put(key, value);
    }

    @Override
    public V remove(final Object key) {
        return this.value.remove(key);
    }

    @Override
    public void putAll(@NotNull final Map<? extends K, ? extends V> m) {
        this.value.putAll(m);
    }

    @Override
    public void clear() {
        this.value.clear();
    }

    @Override
    @NotNull
    public Set<K> keySet() {
        return this.value.keySet();
    }

    @Override
    @NotNull
    public Collection<V> values() {
        return this.value.values();
    }

    @Override
    @NotNull
    public Set<Entry<K, V>> entrySet() {
        return this.value.entrySet();
    }
}
