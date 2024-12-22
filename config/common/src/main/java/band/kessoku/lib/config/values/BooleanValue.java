package band.kessoku.lib.config.values;

import band.kessoku.lib.config.api.Codec;
import band.kessoku.lib.config.api.ConfigValue;

public final class BooleanValue extends ConfigValue<Boolean> {
    public static final Codec<Boolean> CODEC = new Codec<>() {
        @Override
        public Boolean encode(String valueStr) {
            return Boolean.parseBoolean(valueStr);
        }

        @Override
        public String decode(Boolean value) {
            return Boolean.toString(value);
        }
    };

    public BooleanValue() {
        super(CODEC, false);
    }

    public BooleanValue(Codec<Boolean> codec) {
        super(codec, false);
    }

    public BooleanValue(boolean value) {
        super(CODEC, value);
    }

    public BooleanValue(Codec<Boolean> codec, boolean value) {
        super(codec, value);
    }
}
