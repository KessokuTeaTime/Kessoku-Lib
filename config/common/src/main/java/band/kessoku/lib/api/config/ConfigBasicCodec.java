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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import band.kessoku.lib.api.config.api.Codec;
import band.kessoku.lib.api.config.api.ConfigData;
import club.someoneice.json.JSON;
import club.someoneice.json.Pair;
import club.someoneice.json.node.JsonNode;
import club.someoneice.json.node.MapNode;
import club.someoneice.json.processor.JsonBuilder;
import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlParser;
import com.google.common.collect.Maps;

/**
 * @see band.kessoku.lib.api.config.api.Config @Config
 * @see ConfigBasicCodec#register(String, Codec)
 *
 * @author AmarokIce
 */
public final class ConfigBasicCodec {

    private static final Map<String, Codec<Map<String, ConfigData>>> CODECS = Maps.newHashMap();

    private static final Codec<Map<String, ConfigData>> JSON_CODEC = new Codec<>() {
        @Override
        public Map<String, ConfigData> encode(String valueStr) {
            MapNode mapNode = JSON.json5.parse(valueStr).asMapNodeOrEmpty();
            LinkedHashMap<String, ConfigData> data = new LinkedHashMap<>();
            for (Pair<String, JsonNode<?>> pair : mapNode) {
                data.put(pair.getKey(), new ConfigData(pair.getKey(), JsonBuilder.asString(pair.getValue()), Collections.emptyList()));
            }
            return data;
        }

        @Override
        public String decode(Map<String, ConfigData> value) {
            StringBuilder builder = new StringBuilder();
            builder.append("{").append("\n");
            for (ConfigData data : value.values()) {
                builder.append("    ");
                builder.append("\"").append(data.key()).append("\"").append(":")
                        .append(data.rawValue())
                        .append(",").append("\n");
            }
            builder.deleteCharAt(builder.length() - 2);
            builder.append("}");

            return builder.toString();
        }
    };
    private static final Codec<Map<String, ConfigData>> JSON5 = new Codec<>() {
        @Override
        public Map<String, ConfigData> encode(String valueStr) {
            MapNode mapNode = JSON.json5.parse(valueStr).asMapNodeOrEmpty();
            LinkedHashMap<String, ConfigData> data = new LinkedHashMap<>();
            for (Pair<String, JsonNode<?>> pair : mapNode) {
                data.put(pair.getKey(), new ConfigData(pair.getKey(), JsonBuilder.asString(pair.getValue()), Collections.emptyList()));
            }
            return data;
        }

        @Override
        public String decode(Map<String, ConfigData> value) {
            StringBuilder builder = new StringBuilder();
            builder.append("{").append("\n");
            for (ConfigData data : value.values()) {
                builder.append("    ");
                for (String comment : data.comments()) {
                    builder.append("// ").append(comment).append("\n");
                }
                builder.append(data.key()).append(":").append(data.rawValue())
                        .append(",").append("\n");
            }
            builder.deleteCharAt(builder.length() - 2);
            builder.append("}");

            return builder.toString();
        }
    };
    private static final Codec<Map<String, ConfigData>> TOML = new Codec<>() {
        @Override
        public Map<String, ConfigData> encode(String valueStr) {
            LinkedHashMap<String, ConfigData> data = new LinkedHashMap<>();
            Config config = new TomlParser().parse(valueStr);
            config.entrySet().stream()
                    .filter(entry -> Objects.nonNull(entry.getRawValue()))
                    .forEach(it -> data.put(it.getKey(), new ConfigData(it.getKey(),
                                    it.getRawValue().toString(), Collections.emptyList())));
            return data;
        }

        @Override
        public String decode(Map<String, ConfigData> value) {
            StringBuilder builder = new StringBuilder();
            for (ConfigData data : value.values()) {
                for (String comment : data.comments()) {
                    builder.append("# ").append(comment).append("\n");
                }
                builder.append(data.key())
                        .append("=")
                        .append(data.rawValue())
                        .append("\n");
            }
            return builder.toString();
        }
    };

    public static Codec<Map<String, ConfigData>> getCodec(String name) {
        var codec = CODECS.get(name);
        return Objects.isNull(codec) ? JSON5 : codec;
    }

    public static void register(String configCodecName, Codec<Map<String, ConfigData>> codec) {
        CODECS.put(configCodecName, codec);
    }

    static {
        register("json", JSON_CODEC);
        register("json5", JSON5);
    }
}
