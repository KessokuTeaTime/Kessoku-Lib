package band.kessoku.lib.config.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Config certainly allows comment, and multi-line comment. It is usually used to explain the purpose of an entry or to fill in the format, etc. <br>
 * This annotation should to be placed on the config's field. <br>
 * <br>
 * {@snippet :
 * import band.kessoku.lib.config.api.config.Name;
 * import band.kessoku.lib.config.api.config.Comment;
 * import band.kessoku.lib.config.values.config.StringValue;
 *
 * @Config("mymodid")
 * public class MyConfig {
 *      @Comment({"First comment", "Second comment"})
 *      @Name("someoneField")
 *      public static final StringValue SOMEONE_FIELD = new StringValue("test");
 * }
 * }
 * <br>
 * and in config: <br>
 * {@code // First comment} <br>
 * {@code // Second comment} <br>
 * {@code someoneField = test,}
 *
 * @see Config @Config
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Comment {
    String[] value();
}
