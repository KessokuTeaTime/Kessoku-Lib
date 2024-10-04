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

public final class BooleanValue extends DefaultConfigValue<Boolean> {
    private BooleanValue(Supplier<Boolean> defaultValue) {
        super(defaultValue);
    }

    @Override
    public Type getType() {
        return Type.BOOLEAN;
    }

    @Contract("_ -> new")
    public static @NotNull BooleanValue of(boolean bool) {
        return new BooleanValue(() -> bool);
    }

    @Contract("_ -> new")
    public static @NotNull BooleanValue of(Supplier<Boolean> booleanSupplier) {
        return new BooleanValue(booleanSupplier);
    }
}
