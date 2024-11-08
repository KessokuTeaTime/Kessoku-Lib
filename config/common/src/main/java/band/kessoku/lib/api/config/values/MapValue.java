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

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public final class MapValue<K, V> extends DefaultConfigValue<Map<K, V>> implements Map<K, V> {
    private MapValue(Supplier<Map<K, V>> defaultValue) {
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
    @SuppressWarnings({"unchecked"})
    public @NotNull @Unmodifiable Map<K, V> getDefaultFrom() {
        return (ImmutableMap<K, V>) ImmutableMap.builder().putAll(super.getDefaultFrom()).build();
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public @NotNull @Unmodifiable Map<K, V> getDefaultTo() {
        return (ImmutableMap<K, V>) ImmutableMap.builder().putAll(super.getDefaultTo()).build();
    }

    // constructor
    @Contract("_ -> new")
    public static <K, V> @NotNull MapValue<K, V> of(Map<K, V> m) {
        return new MapValue<>(() -> m);
    }

    @Contract("_ -> new")
    public static <K, V> @NotNull MapValue<K, V> of(Supplier<Map<K, V>> mapSupplier) {
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
    public boolean containsKey(Object key) {
        return this.value.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.value.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return this.value.get(key);
    }

    @Override
    public @Nullable V put(K key, V value) {
        return this.value.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return this.value.remove(key);
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        this.value.putAll(m);
    }

    @Override
    public void clear() {
        this.value.clear();
    }

    @Override
    public @NotNull Set<K> keySet() {
        return this.value.keySet();
    }

    @Override
    public @NotNull Collection<V> values() {
        return this.value.values();
    }

    @Override
    public @NotNull Set<Entry<K, V>> entrySet() {
        return this.value.entrySet();
    }
}
