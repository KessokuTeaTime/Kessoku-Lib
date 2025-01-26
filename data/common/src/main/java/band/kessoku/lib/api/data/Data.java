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
package band.kessoku.lib.api.data;

import org.jetbrains.annotations.NotNull;

/**
 * A fundamental part for Kessoku Lib to hold data.
 * <p>
 *     This data is only readable in most cases, and it can be considered immutable.
 * </p>
 * @param <T> The type of data.
 */
public interface Data<T> {
    /**
     * @return The value.
     */
    T get();

    /**
     * @return The id using to distinguish data.
     */
    @NotNull
    String id();
}
