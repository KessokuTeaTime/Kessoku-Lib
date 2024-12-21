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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import band.kessoku.lib.api.config.ConfigValue;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

public final class ListValue<T extends ConfigValue<?, ?>> extends ArrayList<T> implements ConfigValue<List<T>, T[]> {
    private final Consumer<ArrayList<T>> initializeConsumer;
    private ListValue(Consumer<ArrayList<T>> initializeConsumer) {
        super();
        this.initializeConsumer = initializeConsumer;
        initializeConsumer.accept(this);
    }

    // constructor
    @Contract("_ -> new")
    @SafeVarargs
    @NotNull
    public static <E extends ConfigValue<?, ?>> ListValue<E> of(final E... elements) {
        return new ListValue<>(list -> list.addAll(List.of(elements)));
    }

    @Contract("_ -> new")
    @NotNull
    public static <E extends ConfigValue<?, ?>> ListValue<E> of(final List<E> fromList) {
        final List<E> clone = new ArrayList<>(fromList);
        return new ListValue<>(list -> list.addAll(clone));
    }

    @Contract("_ -> new")
    @NotNull
    public static <E extends ConfigValue<?, ?>> ListValue<E> of(final Consumer<ArrayList<E>> initializeConsumer) {
        return new ListValue<>(initializeConsumer);
    }

    @Override
    public void setFrom(List<T> value) {
        this.addAll(value);
    }

    @Override
    public void setTo(T[] value) {
        Collections.addAll(this, value);
    }

    @Override
    @UnmodifiableView
    @SuppressWarnings("RedundantUnmodifiable")
    public List<T> getFrom() {
        return Collections.unmodifiableList(this);
    }

    @Override
    @SuppressWarnings({"unchecked", "DataFlowIssue"})
    public T[] getTo() {
        return (T[]) this.toArray();
    }

    @Override
    public void reset() {
        this.clear();
        this.initializeConsumer.accept(this);
    }

    @Override
    public Type getType() {
        return Type.ARRAY;
    }

    @Override
    public List<T> getDefaultFrom() {
        final ArrayList<T> list = new ArrayList<>();
        initializeConsumer.accept(list);
        return list;
    }

    @Override
    @SuppressWarnings({"unchecked", "DataFlowIssue"})
    public T[] getDefaultTo() {
        return (T[]) getDefaultFrom().toArray();
    }

    public List<Object> normalize() {
        final List<Object> list = new ArrayList<>();
        this.forEach(value -> {
            final Type type = value.getType();
            if (type == Type.ARRAY || type == Type.MAP) {
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

    public static <E extends ConfigValue<V,?>,V> List<E> transform(List<V> list, Function<V,E> boxFunc) {
        final List<E> result = new ArrayList<>();
        list.forEach(v -> {
            result.add(boxFunc.apply(v));
        });
        return result;
    }
}
