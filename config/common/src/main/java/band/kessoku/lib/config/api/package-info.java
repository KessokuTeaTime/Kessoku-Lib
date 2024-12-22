/**
 * The API for config. It all started in {@link band.kessoku.lib.config.api.Config @Config}.<br>
 * <br>
 * {@snippet :
 * import band.kessoku.lib.config.api.config.Name;
 * import band.kessoku.lib.config.api.config.Comment;
 * import band.kessoku.lib.config.values.config.StringValue;
 *
 * @Config("mymodid)
 * public class MyConfig {
 *      @Comment({"First comment", "Second comment"})
 *      @Name("someoneField")
 *      public static final StringValue SOMEONE_FIELD = new StringValue("test");
 * }
 *}
 * <br>
 * and in config: <br>
 * {@code // First comment} <br>
 * {@code // Second comment} <br>
 * {@code someoneField = test,}
 *
 */
package band.kessoku.lib.config.api;