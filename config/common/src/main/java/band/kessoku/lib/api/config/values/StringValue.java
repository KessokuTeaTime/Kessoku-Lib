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

import java.util.function.Supplier;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class StringValue extends DefaultConfigValue<String> {
    private StringValue(final Supplier<String> defaultValue) {
        super(defaultValue);
    }

    @Override
    public Type getType() {
        return Type.STRING;
    }

    @Contract("_ -> new")
    @NotNull
    public static StringValue of(final String s) {
        return new StringValue(() -> s);
    }

    @Contract("_ -> new")
    @NotNull
    public static StringValue of(final Supplier<String> stringSupplier) {
        return new StringValue(stringSupplier);
    }
}
