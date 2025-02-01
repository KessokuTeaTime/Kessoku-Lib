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
package band.kessoku.lib.api.config;

import band.kessoku.lib.api.config.exception.IllegalValueException;

/**
 * {@code Codec} is the key part of data encoding. <br>
 * The something we should to going to do is keep the {@code ConfigValue} agnostic to the real data,
 * the only thing the {@code ConfigValue} has to do is determine the data type,
 * the codec that contains the data, and the rest is a standard container
 * to tell the {@code ConfigValue} not to neglect to handle it.
 *
 * @see ConfigValue
 * @param <T> The type we will encode and decode.
 *
 * @author AmarokIce
 */
public interface Codec<T> {
    /**
     * Encode the raw data (string) to target type data. <br>
     * Usually call by {@link ConfigValue#encode}.
     *
     * @param valueStr The raw data input.
     * @return The target type data.
     * @throws IllegalValueException If config value cannot be encoded, throw IllegalValueException.
     */
    T encode(String valueStr) throws IllegalValueException;

    /**
     * Encode the raw data (string) to target type data. <br>
     * Usually call by {@link ConfigValue#decode()}.
     *
     * @param value The target type data.
     * @return raw data will fill in config.
     */
    String decode(T value);
}
