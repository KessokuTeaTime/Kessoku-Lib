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
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author AmarokIce
 */
public class MapValue extends ConfigValue<Map<String, ConfigValue<?>>> {
    public MapValue(Codec<Map<String, ConfigValue<?>>> codec) {
        super(codec, Maps.newHashMap());
    }

    public MapValue(Codec<Map<String, ConfigValue<?>>> codec, Map<String, ConfigValue<?>> value) {
        super(codec, value);
    }

    /*
    public static final Codec<Map<String, ConfigValue<?>>> CODEC = new Codec<>() {
        @Override
        public Map<String, ConfigValue<?>> encode(String valueStr) {
            return Map.of();
        }

        @Override
        public String decode(Map<String, ConfigValue<?>> value) {
            StringBuilder builder = new StringBuilder();
            builder.append("{");
            for(Map.Entry<String, ConfigValue<?>> entry : value.entrySet()) {
                builder.append(entry.getKey());
                builder.append(":");
                builder.append(entry.getValue().decode());
                builder.append(",");
                builder.append("\n");
            }
            builder.deleteCharAt(builder.length() - 2);
            builder.append("}");
            return builder.toString();
        }
    };
    */
}
