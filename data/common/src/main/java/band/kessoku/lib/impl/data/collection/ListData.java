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
package band.kessoku.lib.impl.data.collection;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import band.kessoku.lib.api.data.Data;
import org.jetbrains.annotations.NotNull;

public class ListData<T> extends AbstractList<T> implements Data<List<T>> {
    private final List<T> list = new ArrayList<>();
    private final String id;

    public ListData(String id) {
        this.id = id;
    }

    public Element<T> element(String id, int index) {
        return new Element<>(id, index, this);
    }

    @Override
    public T set(int index, T element) {
        return list.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        list.add(index, element);
    }

    @Override
    public T remove(int index) {
        return list.remove(index);
    }

    @Deprecated
    @Override
    public List<T> get() {
        return this.list;
    }

    @Override
    public @NotNull String id() {
        return this.id;
    }

    @Override
    public T get(int i) {
        return this.list.get(i);
    }

    @Override
    public int size() {
        return this.list.size();
    }
}
