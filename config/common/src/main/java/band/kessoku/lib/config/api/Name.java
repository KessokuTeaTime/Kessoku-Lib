package band.kessoku.lib.config.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * By default, the ConfigHandler takes the field name as the name of the config field, but you can use this annotation to commit a new name.
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
 *}
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
public @interface Name {
    String value();
}
