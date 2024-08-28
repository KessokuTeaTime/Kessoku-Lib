package band.kessoku.lib.config.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

public sealed class ConfigValue<T> implements Supplier<T> {
    protected T value;
    final T defaultValue;
    List<String> comments = new ArrayList<>();
    @Nullable
    String name;

    private ConfigValue(T value, T defaultValue) {
        this(value, defaultValue, null);
    }

    private ConfigValue(T value, T defaultValue, @Nullable String name) {
        this.value = value;
        this.defaultValue = defaultValue;
        this.name = name;
    }

    public ConfigValue<T> addComment(String comment) {
        this.comments.add(comment);
        return this;
    }

    public void reset() {
        this.value = defaultValue;
    }

    @Override
    public T get() {
        return this.value;
    }

    public void set(T value) {
        this.value = value;
    }

    public static final class StringValue extends ConfigValue<String> {
        public StringValue(String value, String defaultValue) {
            super(value, defaultValue);
        }

        public StringValue(String value, String defaultValue, String name) {
            super(value, defaultValue, name);
        }
    }

    public static final class LongValue extends ConfigValue<Long> {
        public Long max;
        public Long min;

        public LongValue(long value, long defaultValue) {
            super(value, defaultValue);
        }

        public LongValue(long value, long defaultValue, String name) {
            super(value, defaultValue, name);
        }

        public LongValue max(long max) {
            this.max = max;
            return this;
        }

        public void clearMax() {
            this.max = null;
        }

        public LongValue min(long min) {
            this.min = min;
            return this;
        }

        public void clearMin() {
            this.min = null;
        }

        public void clearRange() {
            this.clearMin();
            this.clearMax();
        }

        public void checkMaxRange() {
            if (this.max != null && this.get() >= this.max) this.value = this.max;
        }

        public void checkMinRange() {
            if (this.min != null && this.get() <= this.min) this.value = this.min;
        }

        public void checkRange() {
            this.checkMinRange();
            this.checkMaxRange();
        }

        @Override
        public void set(Long value) {
            this.value = value;
            this.checkRange();
        }
    }

    public static final class DoubleValue extends ConfigValue<Double> {
        public Double max;
        public Double min;
        public DoubleValue(double value, double defaultValue) {
            super(value, defaultValue);
        }

        public DoubleValue(double value, double defaultValue, String name) {
            super(value, defaultValue, name);
        }
        public DoubleValue max(double max) {
            this.max = max;
            return this;
        }

        public void clearMax() {
            this.max = null;
        }

        public DoubleValue min(double min) {
            this.min = min;
            return this;
        }

        public void clearMin() {
            this.min = null;
        }

        public void clearRange() {
            this.clearMin();
            this.clearMax();
        }

        public void checkMaxRange() {
            if (this.max != null && this.get() >= this.max) this.value = this.max;
        }

        public void checkMinRange() {
            if (this.min != null && this.get() <= this.min) this.value = this.min;
        }

        public void checkRange() {
            this.checkMinRange();
            this.checkMaxRange();
        }

        @Override
        public void set(Double value) {
            this.value = value;
            this.checkRange();
        }
    }

    public static final class BooleanValue extends ConfigValue<Boolean> {
        public BooleanValue(boolean value, boolean defaultValue) {
            super(value, defaultValue);
        }

        public BooleanValue(boolean value, boolean defaultValue, String name) {
            super(value, defaultValue, name);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (super.equals(o)) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        ConfigValue<?> that = (ConfigValue<?>) o;
        return Objects.equals(this.value, that.value) && Objects.equals(this.defaultValue, that.defaultValue) && Objects.equals(this.comments, that.comments) && Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value, this.defaultValue, this.comments, this.name);
    }
}
