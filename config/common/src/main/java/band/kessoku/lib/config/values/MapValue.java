package band.kessoku.lib.config.values;

import band.kessoku.lib.config.api.Codec;
import band.kessoku.lib.config.api.ConfigValue;
import com.google.common.collect.Maps;

import java.util.Map;

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
