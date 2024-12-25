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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * By default, the {@code ConfigHandler} takes the field name as the name of the config field, but you can use this annotation to commit a new name.
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
 *
 * @author AmarokIce
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Name {
    String value();
}
