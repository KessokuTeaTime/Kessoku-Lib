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
package band.kessoku.lib.config.serializers;

import band.kessoku.lib.config.api.AbstractConfig;
import band.kessoku.lib.config.api.ConfigSerializer;
import club.someoneice.json.JSON;
import club.someoneice.json.Pair;
import club.someoneice.json.node.JsonNode;
import club.someoneice.json.node.MapNode;
import club.someoneice.json.processor.JsonBuilder;

import java.util.Map;

public class JsonSerializer implements ConfigSerializer {
    @Override
    public String serialize(Map<String, AbstractConfig.ValueWithComment> value) {
        MapNode node = new MapNode();
        value.entrySet().stream()
                .map(it -> new Pair<>(it.getKey(), JsonNode.asJsonNodeOrEmpty(it.getValue().object())))
                .filter(it -> it.getValue().nonNull())
                .forEach(it -> node.put(it.getKey(), it.getValue()));

        return JsonBuilder.prettyPrint(node);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> deserialize(String value) {
        return (Map<String, Object>) JSON.json.parse(value).asTypeNodeOrThrow(JsonNode.NodeType.Map).getObj();
    }

    @Override
    public String getFileExtension() {
        return "json";
    }
}
