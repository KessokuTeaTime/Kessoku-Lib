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
package band.kessoku.lib.config.api.serializers;

import java.util.Map;

import band.kessoku.lib.config.api.AbstractConfig;
import band.kessoku.lib.config.api.ConfigSerializer;
import com.electronwill.nightconfig.core.CommentedConfig;

//todo
public class TomlSerializer implements ConfigSerializer {
    @Override
    public String serialize(Map<String, AbstractConfig.ValueWithComment> value) {
        CommentedConfig config = CommentedConfig.inMemory();
        value.forEach(config::set);
        return "";
    }

    @Override
    public Map<String, Object> deserialize(String value) {
        return Map.of();
    }

    @Override
    public String getFileExtension() {
        return "toml";
    }
}
