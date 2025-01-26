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
package band.kessoku.lib.api.entrypoint;

import band.kessoku.lib.api.platform.Metadata;
import band.kessoku.lib.impl.entrypoint.JavaLanguageAdapter;
import band.kessoku.lib.impl.entrypoint.exceptions.LanguageAdapterException;

public interface LanguageAdapter {
    /**
     * Get an instance of the default language adapter.
     */
    static LanguageAdapter getDefault() {
        return JavaLanguageAdapter.INSTANCE;
    }

    /**
     * Creates an object of {@code type} from an arbitrary string declaration.
     *
     * @param metadata   the mod which the object is from
     * @param value the string declaration of the object
     * @return the created object
     * @throws LanguageAdapterException if a problem arises during creation, such as an invalid declaration
     */
    Object parse(Metadata metadata, String value) throws LanguageAdapterException;
}
