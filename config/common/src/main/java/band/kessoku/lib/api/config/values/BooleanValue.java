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

import band.kessoku.lib.api.config.api.Codec;
import band.kessoku.lib.api.config.api.ConfigValue;

/**
 * @author AmarokIce
 */
public final class BooleanValue extends ConfigValue<Boolean> {
    public static final Codec<Boolean> CODEC = new Codec<>() {
        @Override
        public Boolean encode(String valueStr) {
            return Boolean.parseBoolean(valueStr);
        }

        @Override
        public String decode(Boolean value) {
            return Boolean.toString(value);
        }
    };

    public BooleanValue() {
        super(CODEC, false);
    }

    public BooleanValue(Codec<Boolean> codec) {
        super(codec, false);
    }

    public BooleanValue(boolean value) {
        super(CODEC, value);
    }

    public BooleanValue(Codec<Boolean> codec, boolean value) {
        super(codec, value);
    }
}
