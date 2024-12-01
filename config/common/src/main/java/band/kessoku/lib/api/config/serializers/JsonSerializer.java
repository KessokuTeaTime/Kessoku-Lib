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
package band.kessoku.lib.api.config.serializers;

import java.util.HashMap;
import java.util.Map;

import band.kessoku.lib.api.config.ConfigSerializer;
import band.kessoku.lib.impl.config.AbstractConfig;
import club.someoneice.json.JSON;
import club.someoneice.json.node.JsonNode;

public class JsonSerializer implements ConfigSerializer {
    // TODO
    @Override
    public String serialize(Map<String, AbstractConfig.WrappedValue> valueMap) {
        return "";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> deserialize(String value) {
        Map<String,Object> map = new HashMap<>();
        ((Map<String, JsonNode<?>>) JSON.json.parse(value).asTypeNodeOrThrow(JsonNode.NodeType.Map).getObj()).forEach((key, jsonNode) -> map.put(key, jsonNode.getObj()));
        return map;
    }

    @Override
    public String getFileExtension() {
        return "json";
    }
}
