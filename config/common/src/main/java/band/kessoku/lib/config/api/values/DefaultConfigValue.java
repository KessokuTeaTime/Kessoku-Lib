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

import java.util.function.Supplier;

import band.kessoku.lib.config.api.ConfigValue;

sealed abstract class DefaultConfigValue<T> implements ConfigValue<T, T> permits BooleanValue, DoubleValue, FloatValue, IntegerValue, ListValue, LongValue, MapValue, StringValue {
    public final Supplier<T> defaultValue;
    public T value;

    protected DefaultConfigValue(Supplier<T> defaultValue) {
        this.defaultValue = defaultValue;
        this.reset();
    }

    @Override
    public void setFrom(T value) {
        this.value = value;
    }

    @Override
    public void setTo(T value) {
        this.value = value;
    }

    @Override
    public T getFrom() {
        return this.value;
    }

    @Override
    public T getTo() {
        return this.value;
    }

    @Override
    public void reset() {
        this.value = this.defaultValue.get();
    }

    @Override
    public T getDefaultFrom() {
        return this.defaultValue.get();
    }

    @Override
    public T getDefaultTo() {
        return this.defaultValue.get();
    }
}
