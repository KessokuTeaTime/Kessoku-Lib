package band.kessoku.lib.config.values;

import band.kessoku.lib.config.api.Codec;
import band.kessoku.lib.config.api.ConfigValue;

public final class DoubleValue extends ConfigValue<Double> {
    public static final Codec<Double> CODEC = new Codec<>() {
        @Override
        public Double encode(String valueStr) {
            double output;

            try {
                output = Double.parseDouble(valueStr);
            } catch (NumberFormatException e) {
                output = 0.0;
            }

            return output;
        }

        @Override
        public String decode(Double value) {
            return Double.toString(value);
        }
    };

    public DoubleValue() {
        super(CODEC, 0.0);
    }

    public DoubleValue(Codec<Double> codec) {
        super(codec, 0.0);
    }

    public DoubleValue(double value) {
        super(CODEC, value);
    }

    public DoubleValue(Codec<Double> codec, double value) {
        super(codec, value);
    }
}
