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
package band.kessoku.lib.config.api;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public interface ConfigValue<F, T> extends Supplier<F> {
    void setFrom(F value);

    void setTo(T value);

    F getFrom();

    T getTo();

    @Override
    default F get() {
        return this.getFrom();
    }

    void reset();

    Type getType();

    F getDefaultFrom();

    T getDefaultTo();

    enum Type {
        LIST, MAP, BOOLEAN, STRING, INTEGER, LONG, FLOAT, DOUBLE;

        public static Type asType(Object o) {
            return switch (o) {
                case List<?> ignored -> LIST;
                case Map<?, ?> ignored -> MAP;
                case Boolean ignored -> BOOLEAN;
                case String ignored -> STRING;
                case Long ignored -> LONG;
                case Integer ignored -> INTEGER;
                case Float ignored -> FLOAT;
                case Double ignored -> DOUBLE;
                case null, default -> throw new IllegalArgumentException();
            };
        }
    }
}
