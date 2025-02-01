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
public final class StringValue extends ConfigValue<String> {
    private static final String stringmakr = "\"";
    public static final Codec<String> CODEC = new Codec<>() {
        @Override
        public String encode(String valueStr) {
            return valueStr.startsWith(stringmakr)
                    ? valueStr.substring(1, valueStr.length() - 1)
                    : valueStr;
        }

        @Override
        public String decode(String value) {
            return stringmakr + value + stringmakr;
        }
    };

    public StringValue() {
        super(CODEC, "");
    }

    public StringValue(Codec<String> codec) {
        super(codec, "");
    }

    public StringValue(String value) {
        super(CODEC, value);
    }

    public StringValue(Codec<String> codec, String value) {
        super(codec, value);
    }
}
