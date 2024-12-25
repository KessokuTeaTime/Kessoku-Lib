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
public final class IntegerValue extends ConfigValue<Integer> {
    public static final Codec<Integer> CODEC = new Codec<>() {
        @Override
        public Integer encode(String valueStr) {
            int output;

            try {
                output = Integer.parseInt(valueStr);
            } catch (NumberFormatException e) {
                output = 0;
            }

            return output;
        }

        @Override
        public String decode(Integer value) {
            return Integer.toString(value);
        }
    };

    public IntegerValue() {
        super(CODEC, 0);
    }

    public IntegerValue(Codec<Integer> codec) {
        super(codec, 0);
    }

    public IntegerValue(int value) {
        super(CODEC, value);
    }

    public IntegerValue(Codec<Integer> codec, int value) {
        super(codec, value);
    }
}
