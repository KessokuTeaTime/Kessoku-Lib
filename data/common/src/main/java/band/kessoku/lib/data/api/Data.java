package band.kessoku.lib.data.api;

import org.jetbrains.annotations.NotNull;

/**
 * A fundamental part for Kessoku Lib to hold data.
 * <p>
 *     This data is only readable in most cases, and it can be considered immutable.
 * </p>
 * @param <T> The type of data.
 */
public interface Data<T> {
    /**
     * @return The value.
     */
    T get();

    /**
     * @return The id using to distinguish data.
     */
    @NotNull
    String id();
}
