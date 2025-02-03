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

import java.util.Map;

import band.kessoku.lib.api.config.Codec;
import band.kessoku.lib.api.config.ConfigValue;
import band.kessoku.lib.api.config.exception.IllegalValueException;
import club.someoneice.json.JSON;
import club.someoneice.json.Pair;
import club.someoneice.json.node.JsonNode;
import club.someoneice.json.node.MapNode;
import club.someoneice.json.processor.JsonBuilder;
import com.google.common.collect.Maps;

/**
 * These data structures have different syntax depending on the config type, <br>
 * so there is no default {@code codec}, and they usually need to be implemented manually through requirements <br>
 * (or by introducing a parser in the target format, like nightconfig's toml codec).
 *
 * @see Codec
 *
 * @author AmarokIce
 */
public class MapValue extends ConfigValue<Map<String, ConfigValue<?>>> {
    public static final Codec<Map<String, ConfigValue<?>>> JSON_CODEC = new Codec<>() {
        @Override
        public Map<String, ConfigValue<?>> encode(String valueStr) throws IllegalValueException {
            Map<String, ConfigValue<?>> map = Maps.newHashMap();
            JSON.json5.parse(valueStr).asMapNodeOrEmpty().stream()
                    .map(it ->
                            new Pair<>(it.getKey(), new StringValue(StringValue.CODEC, it.getValue().toString())))
                    .forEach(it -> map.put(it.getKey(), it.getValue()));
            return map;
        }

        @Override
        public String decode(Map<String, ConfigValue<?>> value) {
            MapNode node = new MapNode();
            value.forEach((key, valueNode) -> {
                Object v = valueNode.decode();
                node.put(key, new JsonNode<>(v));
            });
            return JsonBuilder.prettyPrint(node);
        }
    };

    public MapValue(Map<String, ConfigValue<?>> value) {
        super(JSON_CODEC, value);
    }

    public MapValue(Codec<Map<String, ConfigValue<?>>> codec) {
        super(codec, Maps.newHashMap());
    }

    public MapValue(Codec<Map<String, ConfigValue<?>>> codec, Map<String, ConfigValue<?>> value) {
        super(codec, value);
    }
}
