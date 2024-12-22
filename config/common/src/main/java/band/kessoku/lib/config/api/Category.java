package band.kessoku.lib.config.api;

/**
 * Category is a sub config builder. Everything same as {@code Config} class. <br>
 * Format by father config, allow another category in this category. <br>
 * We're suggestion to be every field in category mark {@code static} like config.
 */
public class Category extends ConfigValue<Void> {
    protected Category() {
        super(null, null);
    }
}
