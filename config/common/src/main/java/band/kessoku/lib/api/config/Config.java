/*
 * Copyright (c) 2024, 2025 KessokuTeaTime
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
package band.kessoku.lib.api.config;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import band.kessoku.lib.api.config.serializer.ConfigSerializers;

/**
 * All configs started here to create a new config.
 *
 * <p>The only necessary data is modid, if config name is empty, it will fill by modid.
 * The registration process is automatic, you need to put the secondary bet liberation
 * at the head of the config class.
 *
 * <p>We provide some default {@link ConfigSerializer} in {@link ConfigSerializers}.
 *
 * <p>And the new config {@code ConfigSerializer} should registry in {@link ConfigSerializers#register}.
 *
 * {@snippet :
 * @Config(modid="mymodid", serialize="json5")
 * public class MyConfig {
 *      @Comment("First comment")
 *      @Comment("Second comment")
 *      @Name("someoneField")
 *      public static String SOMEONE_FIELD = "test";
 * }
 *}
 *
 * <p>and in config:
 *
 * {@snippet :
 * // First comment
 * // Second comment
 * someoneField = test
 * }
 *
 * @see Comment
 * @see Name
 * @see ConfigSerializers
 *
 * @author AmarokIce
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
    String modid();
    String name() default "";
    String serialize() default "toml";
}
