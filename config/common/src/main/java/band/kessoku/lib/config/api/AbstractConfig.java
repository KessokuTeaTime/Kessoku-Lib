package band.kessoku.lib.config.api;

import java.util.Objects;

public abstract class AbstractConfig {
    protected String parent;
    protected String fileName;

    public AbstractConfig(String fileName) {
        this("", fileName);
    }

    public AbstractConfig(String parent, String fileName) {
        this.parent = Objects.requireNonNull(parent);
        this.fileName = Objects.requireNonNull(fileName);
    }

    protected ConfigValue.StringValue newString(String value) {
        return new ConfigValue.StringValue(value, value);
    }

    protected ConfigValue.StringValue newString(String value, String name) {
        return new ConfigValue.StringValue(value, value, name);
    }

    protected ConfigValue.LongValue newLong(long value, String name) {
        return new ConfigValue.LongValue(value, value, name);
    }

    protected ConfigValue.LongValue newLong(long value) {
        return new ConfigValue.LongValue(value, value);
    }

    protected ConfigValue.DoubleValue newDouble(double value, String name) {
        return new ConfigValue.DoubleValue(value, value, name);
    }

    protected ConfigValue.DoubleValue newDouble(double value) {
        return new ConfigValue.DoubleValue(value, value);
    }
}
