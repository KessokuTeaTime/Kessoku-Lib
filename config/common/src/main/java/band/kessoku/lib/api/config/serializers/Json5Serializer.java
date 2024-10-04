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

import java.util.Map;

import band.kessoku.lib.api.config.AbstractConfig;
import band.kessoku.lib.api.config.ConfigSerializer;
import club.someoneice.json.JSON;
import club.someoneice.json.node.JsonNode;
import club.someoneice.json.processor.Json5Builder;

// TODO
public class Json5Serializer implements ConfigSerializer {
    @Override
    public String serialize(Map<String, AbstractConfig.ValueWithComment> value) {
        return "";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> deserialize(String value) {
        return (Map<String, Object>) JSON.json5.parse(value).asTypeNodeOrThrow(JsonNode.NodeType.Map).getObj();
    }

    @Override
    public String getFileExtension() {
        return "json5";
    }

    private Json5Builder.ObjectBean toBean(Map<String, AbstractConfig.ValueWithComment> value) {
        Json5Builder builder = new Json5Builder();
        Json5Builder.ObjectBean objectBean = builder.getObjectBean();
        value.forEach((s, valueWithComment) -> {
            for (String comment : valueWithComment.comments()) {
                objectBean.addNote(comment);
            }

            if (valueWithComment.object() instanceof Map<?, ?>) {
                objectBean.addBean(s, this.toBean((Map<String, AbstractConfig.ValueWithComment>) valueWithComment.object()));
            }
        });
        return objectBean;
    }
}
