/*
 * Copyright (c) 2024 KessokuTeaTime
 *
 * Licensed under the GNU Lesser General Pubic License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package band.kessoku.lib.api.config.api;

import band.kessoku.lib.api.config.ConfigBasicCodec;

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
 *
 * @author AmarokIce
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
    String value();
    String name() default "";
    String codec() default "json5";
}
