package band.kessoku.lib.config.api;

import club.someoneice.json.JSON;
import club.someoneice.json.Pair;
import club.someoneice.json.node.JsonNode;
import club.someoneice.json.node.MapNode;
import club.someoneice.json.processor.JsonBuilder;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @see Config @Config
 * @see ConfigBasicCodec#register(String, Codec)
 */
public final class ConfigBasicCodec {

    private static final Map<String, Codec<Map<String, ConfigData>>> CODECS = new LinkedHashMap<>();

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
                builder.append("\"").append(data.key()).append("\"")
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
                builder.append(data.key()).append(data.rawValue())
                        .append(",").append("\n");
            }
            builder.deleteCharAt(builder.length() - 2);
            builder.append("}");

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
