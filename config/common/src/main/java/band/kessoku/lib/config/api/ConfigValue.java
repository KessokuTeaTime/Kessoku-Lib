package band.kessoku.lib.config.api;

import band.kessoku.lib.config.ConfigHandler;
import band.kessoku.lib.config.values.*;

import java.util.Objects;

/**
 * ConfigValue is just a data container,
 * any data processing should use {@link Codec}.
 *
 * @see Codec
 * @see ConfigValue#ConfigValue(Codec, Object) 
 * @param <T>
 */
public abstract class ConfigValue<T> {
    public final Codec<T> codec;
    private T value;

    /**
     * We don't recommend creating a dangling {@code ConfigValue} by lambda. <br>
     * Creating a fixed class for {@code ConfigValue} is beneficial for describing the data correctly and avoiding misperceptions or {@code Codec} mismatches.
     * <br>
     * And here are basic values:
     * <br> {@link StringValue StringValue}
     * <br> {@link IntegerValue IntegerValue}
     * <br> {@link BooleanValue BooleanValue}
     * <br> {@link DoubleValue DoubleValue}
     * <br> {@link ArrayValue ArrayValue}
     * <br> {@link MapValue MapValue}
     * <br> {@link IdentifierValue IdentifierValue}
     * <br>
     * And maybe you want to know the sub-config type {@link Category}.
     *
     * @see Category
     * @see ConfigHandler Config
     *
     * @param codec From value to raw data,or from raw data to value. See {@link Codec}
     * @param object The value data.
     */
    protected ConfigValue(Codec<T> codec, T object) {
        this.codec = codec;
        this.value = object;
    }

    public final void setValue(T object) {
        this.value = object;
    }

    public final T getValue() {
        return value;
    }

    public void encode(String valueStr) {
        this.setValue(codec.encode(valueStr));
    }

    public String decode() {
        return codec.decode(this.value);
    }

    @Override
    public String toString() {
        return this.decode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigValue<?> that = (ConfigValue<?>) o;
        return Objects.equals(codec, that.codec) && Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}