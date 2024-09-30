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

import band.kessoku.lib.api.data.MutableData;
import org.jetbrains.annotations.NotNull;

public class Element<T> implements MutableData<T> {
    public final String id;
    public final int index;
    protected final ListData<T> listData;

    public Element(String id, int index, ListData<T> listData) {
        this.id = id;
        this.index = index;
        this.listData = listData;
    }

    @Override
    public void set(T newValue) {
        listData.set(index, newValue);
    }

    @Override
    public T get() {
        return listData.get(index);
    }

    @Override
    public @NotNull String id() {
        return id;
    }
}
