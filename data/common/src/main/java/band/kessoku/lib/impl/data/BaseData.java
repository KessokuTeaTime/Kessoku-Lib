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
package band.kessoku.lib.impl.data;

import java.util.Objects;

import band.kessoku.lib.api.data.MutableData;
import org.jetbrains.annotations.NotNull;

public class BaseData<T> implements MutableData<T> {
    private final String id;
    private T value;

    public BaseData(String id, T defaultValue) {
        this.id = id;
        this.value = defaultValue;
    }

    @Override
    public void set(T newValue) {
        this.value = newValue;
    }

    @Override
    public T get() {
        return this.value;
    }

    @Override
    public @NotNull String id() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseData<?> baseData)) return false;
        return Objects.equals(value, baseData.value) && Objects.equals(id, baseData.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, id);
    }
}
