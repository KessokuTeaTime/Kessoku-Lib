package band.kessoku.lib.config.values;

import band.kessoku.lib.config.api.Codec;
import band.kessoku.lib.config.api.ConfigValue;

public final class IntegerValue extends ConfigValue<Integer> {
    public static final Codec<Integer> CODEC = new Codec<>() {
        @Override
        public Integer encode(String valueStr) {
            int output;

            try {
                output = Integer.parseInt(valueStr);
            } catch (NumberFormatException e) {
                output = 0;
            }

            return output;
        }

        @Override
        public String decode(Integer value) {
            return Integer.toString(value);
        }
    };

    public IntegerValue() {
        super(CODEC, 0);
    }

    public IntegerValue(Codec<Integer> codec) {
        super(codec, 0);
    }

    public IntegerValue(int value) {
        super(CODEC, value);
    }

    public IntegerValue(Codec<Integer> codec, int value) {
        super(codec, value);
    }
}
