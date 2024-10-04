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
package band.kessoku.lib.config.api.values;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.function.Supplier;

@SuppressWarnings({"rawtypes"})
public final class ListValue<T> extends DefaultConfigValue<List<T>> implements List<T> {
    private ListValue(Supplier<List<T>> defaultValue) {
        super(defaultValue);
    }

    @Override
    public Type getType() {
        return Type.LIST;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void reset() {
        this.value = new ArrayList(this.defaultValue.get());
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public @NotNull @Unmodifiable List<T> getDefaultFrom() {
        return (ImmutableList<T>) ImmutableList.builder().add(super.getDefaultFrom()).build();
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public @NotNull @Unmodifiable List<T> getDefaultTo() {
        return (ImmutableList<T>) ImmutableList.builder().add(super.getDefaultTo()).build();
    }

    // constructor
    @Contract("_ -> new")
    @SafeVarargs
    public static <E> @NotNull ListValue<E> of(E... elements) {
        return new ListValue<>(() -> new ArrayList<>(List.of(elements)));
    }

    @Contract("_ -> new")
    public static <E> @NotNull ListValue<E> of(List<E> list) {
        return new ListValue<>(() -> new ArrayList<>(list));
    }

    @Contract("_ -> new")
    public static <E> @NotNull ListValue<E> of(Supplier<List<E>> listSupplier) {
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
    public boolean contains(Object o) {
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
    public <T1> T1 @NotNull [] toArray(T1 @NotNull [] a) {
        return this.value.toArray(a);
    }

    @Override
    public boolean add(T t) {
        return this.value.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return this.value.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        /* Amarok Note:
        *   ArrayList 的数据存储量不可知的情况下，悲观假设有大量数据，那么 ArrayList#containsAll 的效率会低的令人发指...
        *   虽说如果数据量少的话套一层 Set 也是无端浪费性能... 但我依然建议悲观演算。
        * */
        return Sets.newHashSet(this.value).containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        return this.value.addAll(c);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        return this.value.addAll(index, c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return this.value.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return this.value.retainAll(c);
    }

    @Override
    public void clear() {
        this.value.clear();
    }

    @Override
    public T get(int index) {
        return this.value.get(index);
    }

    @Override
    public T set(int index, T element) {
        return this.value.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        this.value.add(index, element);
    }

    @Override
    public T remove(int index) {
        return this.value.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.value.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.value.lastIndexOf(o);
    }

    @Override
    public @NotNull ListIterator<T> listIterator() {
        return this.value.listIterator();
    }

    @Override
    public @NotNull ListIterator<T> listIterator(int index) {
        return this.value.listIterator(index);
    }

    @Override
    public @NotNull List<T> subList(int fromIndex, int toIndex) {
        return this.value.subList(fromIndex, toIndex);
    }
}
