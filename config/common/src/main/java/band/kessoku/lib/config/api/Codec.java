package band.kessoku.lib.config.api;

/**
 * {@code Codec} is the key part of data encoding. <br>
 * The something we should to going to do is keep the {@code ConfigValue} agnostic to the real data,
 * the only thing the {@code ConfigValue} has to do is determine the data type,
 * the codec that contains the data, and the rest is a standard container
 * to tell the {@code ConfigValue} not to neglect to handle it.
 *
 * @see ConfigValue
 * @param <T> The type we will encode and decode.
 */
public interface Codec<T> {
    /**
     * Encode the raw data (string) to target type data. <br>
     * Usually call by {@link ConfigValue#encode}.
     *
     * @param valueStr The raw data input.
     * @return The target type data.
     */
    T encode(String valueStr);

    /**
     * Encode the raw data (string) to target type data. <br>
     * Usually call by {@link ConfigValue#decode()}.
     *
     * @param value The target type data.
     * @return raw data will fill in config.
     */
    String decode(T value);
}
