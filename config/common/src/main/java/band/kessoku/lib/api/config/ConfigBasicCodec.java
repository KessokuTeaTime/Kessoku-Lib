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

import club.someoneice.json.JSON;
import club.someoneice.json.Pair;
import club.someoneice.json.node.JsonNode;
import club.someoneice.json.node.MapNode;
import club.someoneice.json.processor.JsonBuilder;
import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlParser;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * @see band.kessoku.lib.api.config.Config @Config
 * @see ConfigBasicCodec#register(String, Codec)
 *
 * @author AmarokIce
 */
public final class ConfigBasicCodec {

    private static final Map<String, Codec<Map<String, ConfigData>>> CODECS = Maps.newHashMap();

    public static final Function<ConfigData, String> JSON_FORMATTER = it ->
            String.format("\"%s\"", it.key()) + ":" + it.rawValue();

    public static final Function<ConfigData, String> JSON5_FORMATTER = it -> {
        StringBuilder builder = new StringBuilder();
        it.comments().forEach(comment -> builder.append("// ").append(comment).append("\n"));
        builder.append(String.format("\"%s\"", it.key()))
                .append(":")
                .append(it.rawValue());
        return builder.toString();
    };

    public static final Function<ConfigData, String> TOML_FORMATTER = it -> {
        StringBuilder builder = new StringBuilder();
        it.comments().forEach(commit -> builder
                .append("# ")
                .append(commit)
                .append("\n"));

        builder.append(it.key())
                .append("=")
                .append(it.rawValue());

        return builder.toString();
    };

    private static final Codec<Map<String, ConfigData>> JSON_CODEC = new Codec<>() {
        @Override
        public Map<String, ConfigData> encode(String valueStr) {
            MapNode mapNode = JSON.json.parse(valueStr).asMapNodeOrEmpty();
            LinkedHashMap<String, ConfigData> data = new LinkedHashMap<>();
            for (Pair<String, JsonNode<?>> pair : mapNode) {
                data.put(pair.getKey(), new ConfigData(pair.getKey(), JsonBuilder.asString(pair.getValue()),
                        Collections.emptyList()));
            }
            return data;
        }

        @Override
        public String decode(Map<String, ConfigData> value) {
            StringBuilder builder = new StringBuilder();
            builder.append("{").append("\n");
            value.values().forEach(it -> builder
                    .append("    ")
                    .append(it.toString(JSON_FORMATTER))
                    .append(",").append("\n"));

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
                data.put(pair.getKey(), new ConfigData(pair.getKey(), JsonBuilder.asString(pair.getValue()),
                        Collections.emptyList()));
            }
            return data;
        }

        @Override
        public String decode(Map<String, ConfigData> value) {
            StringBuilder builder = new StringBuilder();
            builder.append("{").append("\n");
            value.values().forEach(it -> builder
                    .append("    ")
                    .append(it.toString(JSON5_FORMATTER))
                    .append(",").append("\n"));

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
                    .forEach(it -> data.put(it.getKey(), new ConfigData(it.getKey(), it.getRawValue().toString(),
                            Collections.emptyList())));
            return data;
        }

        @Override
        public String decode(Map<String, ConfigData> value) {
            StringBuilder builder = new StringBuilder();

            value.values().forEach(it ->
                builder.append(it.toString(TOML_FORMATTER)).append("\n"));

            return builder.toString();
        }
    };

    public static Codec<Map<String, ConfigData>> getCodec(String name) throws ClassNotFoundException {
        var codec = CODECS.get(name);
        if (Objects.isNull(codec)) {
            throw new ClassNotFoundException("Can't find the type " + name + "!");
        }
        return codec;
    }

    public static void register(String configCodecName, Codec<Map<String, ConfigData>> codec) {
        CODECS.put(configCodecName, codec);
    }

    static {
        register("json", JSON_CODEC);
        register("json5", JSON5);
        register("toml", TOML);
    }
}
