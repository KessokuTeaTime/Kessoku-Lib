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

import band.kessoku.lib.api.config.exception.IllegalValueException;
import band.kessoku.lib.api.config.serializer.Json5ConfigSerializer;
import band.kessoku.lib.api.config.serializer.JsonConfigSerializer;
import band.kessoku.lib.api.config.serializer.TomlConfigSerializer;

/**
 * @see JsonConfigSerializer
 * @see Json5ConfigSerializer
 * @see TomlConfigSerializer
 *
 * @author AmarokIce
 */
public interface ConfigSerializer {
    /**
     * @throws IllegalValueException If config name cannot be encoded, throw IllegalValueException.
     */
    void serializer(String valueStr, Class<?> clazz) throws IllegalValueException;

    /**
     * @return raw data will fill in config.
     */
    String deserializer(Class<?> clazz);
}
