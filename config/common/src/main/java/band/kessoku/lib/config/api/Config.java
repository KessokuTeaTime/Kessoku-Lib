package band.kessoku.lib.config.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * All configs started here. To create a new config. <br>
 * The only necessary data is modid, if config name is empty, it will fill by modid. <br>
 * The registration process is automatic, you just need to put the secondary bet liberation at the head of the config class. <br>
 * We provide some default {@code Codec} in {@link ConfigBasicCodec}, and the new config codec should registry in {@link ConfigBasicCodec#register}.
 * <br>
 * Example:
 * {@snippet :
 *  @Config("mymodid")
 *  public class MyConfig {
 *    // Other code...
 *  }
 * }
 *
 * @see Name @Name
 * @see Comment @Comment
 * @see ConfigBasicCodec#register(String, Codec)
 * @see ConfigValue
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
    String value();
    String name() default "";
    String codec() default "json5";
}
