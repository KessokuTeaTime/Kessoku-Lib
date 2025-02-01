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

/**
 * The API for config. You can use annotations to create a config and save it to any file format you like.<br>
 * It all started in {@link band.kessoku.lib.api.config.api.Config @Config}.<br>
 * {@snippet :
 * import band.kessoku.lib.config.config.Name;
 * import band.kessoku.lib.config.config.Comment;
 * import band.kessoku.lib.config.values.config.StringValue;
 *
 * @Config("mymodid)
 * public class MyConfig {
 *      @Comment("First comment")
 *      @Comment("Second comment")
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
 * @see band.kessoku.lib.api.config.api.Config Config
 * @see band.kessoku.lib.api.config.api.ConfigValue ConfigValue
 */
package band.kessoku.lib.api.config;