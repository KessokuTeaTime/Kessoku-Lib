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

import band.kessoku.lib.api.config.Codec;
import band.kessoku.lib.api.config.ConfigValue;

/**
 * @author AmarokIce
 */
public final class DoubleValue extends ConfigValue<Double> {
    public static final Codec<Double> CODEC = new Codec<>() {
        @Override
        public Double encode(String valueStr) {
            double output;

            try {
                output = Double.parseDouble(valueStr);
            } catch (NumberFormatException e) {
                output = 0.0;
            }

            return output;
        }

        @Override
        public String decode(Double value) {
            return Double.toString(value);
        }
    };

    public DoubleValue() {
        super(CODEC, 0.0);
    }

    public DoubleValue(Codec<Double> codec) {
        super(codec, 0.0);
    }

    public DoubleValue(double value) {
        super(CODEC, value);
    }

    public DoubleValue(Codec<Double> codec, double value) {
        super(codec, value);
    }
}
