package band.kessoku.lib.config.values;

import band.kessoku.lib.config.api.Codec;
import band.kessoku.lib.config.api.ConfigValue;

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
