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
import java.util.function.Supplier;

import band.kessoku.lib.api.config.ConfigValue;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.sun.jna.internal.ReflectionUtils;
import io.netty.util.internal.ReflectionUtil;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

@SuppressWarnings({"rawtypes"})
public final class ListValue<T extends ConfigValue<?, ?>> extends DefaultConfigValue<List<T>> implements List<T> {
    private ListValue(final Supplier<List<T>> defaultValue) {
        super(defaultValue);
    }

    @Override
    public Type getType() {
        return Type.ARRAY;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void reset() {
        this.value = new ArrayList(this.defaultValue.get());
    }

    @Override
    @NotNull
    @Unmodifiable
    public List<T> getDefaultFrom() {
        return Collections.unmodifiableList(super.getDefaultFrom());
    }

    @Override
    @NotNull
    @Unmodifiable
    public List<T> getDefaultTo() {
        return Collections.unmodifiableList(super.getDefaultTo());
    }

    public List<?> normalize() {
        List list = new ArrayList<>();
        this.value.forEach(value -> {
            if (List.of(Type.ARRAY,Type.MAP).contains(value.getType())) {
                try {
                    list.add(MethodUtils.getAccessibleMethod(value.getClass(),"normalize").invoke(value));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            } else {
                list.add(value.getTo());
            }
        });
        return list;
    }

    // constructor
    @Contract("_ -> new")
    @SafeVarargs
    @NotNull
    public static <E extends ConfigValue<?, ?>> ListValue<E> of(final E... elements) {
        return new ListValue<>(() -> new ArrayList<>(List.of(elements)));
    }

    @Contract("_ -> new")
    @NotNull
    public static <E extends ConfigValue<?, ?>> ListValue<E> of(final List<E> list) {
        return new ListValue<>(() -> new ArrayList<>(list));
    }

    @Contract("_ -> new")
    @NotNull
    public static <E extends ConfigValue<?, ?>> ListValue<E> of(final Supplier<List<E>> listSupplier) {
        return new ListValue<>(listSupplier);
    }

    // List
    @Override
    public int size() {
        return this.value.size();
    }

    @Override
    public boolean isEmpty() {
        return this.value.isEmpty();
    }

    @Override
    public boolean contains(final Object o) {
        return this.value.contains(o);
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return this.value.iterator();
    }

    @Contract(pure = true)
    @Override
    public Object @NotNull [] toArray() {
        return this.value.toArray();
    }

    @Override
    public <T1> T1 @NotNull [] toArray(final T1 @NotNull [] a) {
        return this.value.toArray(a);
    }

    @Override
    public boolean add(final T t) {
        return this.value.add(t);
    }

    @Override
    public boolean remove(final Object o) {
        return this.value.remove(o);
    }

    @Override
    @SuppressWarnings("SlowListContainsAll")
    public boolean containsAll(@NotNull final Collection<?> c) {
        return this.value.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull final Collection<? extends T> c) {
        return this.value.addAll(c);
    }

    @Override
    public boolean addAll(final int index, @NotNull final Collection<? extends T> c) {
        return this.value.addAll(index, c);
    }

    @Override
    public boolean removeAll(@NotNull final Collection<?> c) {
        return this.value.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull final Collection<?> c) {
        return this.value.retainAll(c);
    }

    @Override
    public void clear() {
        this.value.clear();
    }

    @Override
    public T get(final int index) {
        return this.value.get(index);
    }

    @Override
    public T set(final int index, final T element) {
        return this.value.set(index, element);
    }

    @Override
    public void add(final int index, final T element) {
        this.value.add(index, element);
    }

    @Override
    public T remove(final int index) {
        return this.value.remove(index);
    }

    @Override
    public int indexOf(final Object o) {
        return this.value.indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return this.value.lastIndexOf(o);
    }

    @Override
    @NotNull
    public ListIterator<T> listIterator() {
        return this.value.listIterator();
    }

    @Override
    @NotNull
    public ListIterator<T> listIterator(final int index) {
        return this.value.listIterator(index);
    }

    @Override
    @NotNull
    public List<T> subList(final int fromIndex, final int toIndex) {
        return this.value.subList(fromIndex, toIndex);
    }
}
