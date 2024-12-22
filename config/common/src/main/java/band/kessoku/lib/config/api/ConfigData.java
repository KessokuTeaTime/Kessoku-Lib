package band.kessoku.lib.config.api;

import java.util.List;

/**
 * Record the config's raw data, by encode or decode from config. <br>
 * Determine the {@link Name name}, {@link ConfigValue data wrapping}, {@link Comment comment},
 *
 * @param key The key of config field.
 * @param rawValue The raw data of config field's value.
 * @param comments The comment for config field.
 */
public record ConfigData(String key, String rawValue, List<String> comments) {
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for(String comment: comments) {
            builder.append("//").append(comment).append("\n");
        }

        builder.append(key).append("=").append(rawValue);

        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        return this.getClass() == o.getClass() && this.hashCode() == o.hashCode();
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}
