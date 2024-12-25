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

/**
 * Category is a sub config builder. Everything same as {@code Config} class. <br>
 * Format by father config, allow another category in this category. <br>
 * We're suggestion to be every field in category mark {@code static} like config.
 *
 * @author AmarokIce
 */
public class Category extends ConfigValue<Void> {
    protected Category() {
        super(null, null);
    }
}
