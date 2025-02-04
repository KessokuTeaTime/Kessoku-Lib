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
package band.kessoku.lib.api.config;

import java.lang.annotation.*;

/**
 * Config certainly allows comment, and multi-line comment.
 * It is usually used to explain the purpose of an entry or to fill in the format, etc.
 *
 * <p>This annotation should be placed on the config's field.
 *
 * {@snippet :
 * import band.kessoku.lib.config.api.config.Config;
 * import band.kessoku.lib.config.api.config.Name;
 * import band.kessoku.lib.config.api.config.Comments;
 * import band.kessoku.lib.config.values.config.StringValue;
 *
 * @Config(modid="mymodid", serialize="json5")
 * public class MyConfig {
 *      @Comment("First comment")
 *      @Comment("Second comment")
 *      @Name("someoneField")
 *      public static final StringValue SOMEONE_FIELD = new StringValue("test");
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
 * @see Name @Name
 *
 * @author AmarokIce
 */
@Repeatable(Comments.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Comment {
    String value();
}
