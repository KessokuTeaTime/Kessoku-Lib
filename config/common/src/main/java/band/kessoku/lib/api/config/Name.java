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

/**
 * By default, the {@code ConfigHandler} takes the field name as the name of the config field,
 * but you can use this annotation to commit a new name.
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
 * @see Config @Config
 * @see Comment @Comment
 *
 * @author AmarokIce
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Name {
    String value();
}
