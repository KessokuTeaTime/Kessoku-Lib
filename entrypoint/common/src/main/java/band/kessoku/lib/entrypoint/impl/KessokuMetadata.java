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
package band.kessoku.lib.entrypoint.impl;

import band.kessoku.lib.entrypoint.KessokuEntrypoint;
import band.kessoku.lib.entrypoint.api.LanguageAdapter;
import band.kessoku.lib.entrypoint.impl.exceptions.KessokuParseException;
import club.someoneice.json.Pair;
import club.someoneice.json.api.exception.NodeCastException;
import club.someoneice.json.node.ArrayNode;
import club.someoneice.json.node.JsonNode;
import club.someoneice.json.node.MapNode;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public record KessokuMetadata(Map<String, List<EntrypointMetadata>> entrypoints,
                              Map<String, LanguageAdapter> languageAdapters, String modid) {
    public KessokuMetadata(Map<String, List<EntrypointMetadata>> entrypoints, Map<String, LanguageAdapter> languageAdapters, String modid) {
        this.entrypoints = Collections.unmodifiableMap(entrypoints);
        this.languageAdapters = Collections.unmodifiableMap(languageAdapters);
        this.modid = modid;
    }

    public static KessokuMetadata parse(final MapNode json, final String modid) {
        final Map<String, List<EntrypointMetadata>> entrypoints = new HashMap<>();
        final Map<String, LanguageAdapter> languageAdapters = new HashMap<>();
        try {
            for (Pair<String, JsonNode<?>> pair : json) {
                switch (pair.getKey()) {
                    case "entrypoint" -> parseEntrypoint(entrypoints, pair.getValue(), modid);
                    case "languageAdapters" -> parseLanguageAdapters(languageAdapters, pair.getValue(), modid);
                    case null, default -> throw new IllegalArgumentException();
                }
            }
            return new KessokuMetadata(entrypoints, languageAdapters,modid);
        } catch (Exception e) {
            throw new KessokuParseException(e, "Failed to parse kessoku.json provided by " + modid);
        }
    }

    private static void parseEntrypoint(final Map<String, List<EntrypointMetadata>> entrypoints, final JsonNode<?> node, final String modid) {
        // Check if it's an object
        node.asTypeNodeOrThrow(JsonNode.NodeType.Map, "entrypoint should be an object!");
        ((MapNode) node).forEach(pair -> {
            // normalize value
            final JsonNode<?> rawValue = pair.getValue();
            final List<EntrypointMetadata> entrypointMetadataList = switch (rawValue.getType()) {
                case Map -> parseObjectEntrypoint((MapNode) rawValue);
                case String -> List.of(new EntrypointMetadata() {
                    @Override
                    public String getAdapter() {
                        return "java";
                    }

                    @Override
                    public String getValue() {
                        return (String) rawValue.getObj();
                    }
                });
                case Array -> {
                    ArrayNode arrayNode = (ArrayNode) rawValue;
                    if (arrayNode.isEmpty()) {
                        yield List.of();
                    } else {
                        yield switch (arrayNode.get(0).getType()) {
                            case String -> {
                                List<EntrypointMetadata> result = new ArrayList<>();
                                arrayNode.forEach(jsonNode -> result.add(new EntrypointMetadata() {
                                    @Override
                                    public String getAdapter() {
                                        return "java";
                                    }

                                    @Override
                                    public String getValue() {
                                        return (String) jsonNode.getObj();
                                    }
                                }));
                                yield result;
                            }
                            case Map -> {
                                List<EntrypointMetadata> list = new ArrayList<>();
                                arrayNode.forEach(jsonNode -> list.addAll(parseObjectEntrypoint((MapNode) jsonNode)));
                                yield list;
                            }
                            case null, default -> throw new NodeCastException("");
                        };
                    }
                }
                case null, default -> throw new NodeCastException("");
            };
            entrypoints.put(pair.getKey(), entrypointMetadataList);
        });
    }

    private static List<EntrypointMetadata> parseObjectEntrypoint(MapNode node) {
        Map<String, JsonNode<?>> map = node.getObj();
        String adapter = "java";
        String value = null;
        for (Map.Entry<String, JsonNode<?>> entry : map.entrySet()) {
            String s = entry.getKey();
            JsonNode<?> jsonNode = entry.getValue();
            switch (s) {
                case "adapter":
                    adapter = (String) jsonNode.asTypeNodeOrThrow(JsonNode.NodeType.String, "adapter should be a string").getObj();
                    break;
                case "value":
                    value = (String) jsonNode.asTypeNodeOrThrow(JsonNode.NodeType.String, "value should be a string").getObj();
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown key " + s + " found in kessoku.json!");
            }
        }
        if (value == null) throw new NullPointerException();
        final String finalAdapter = adapter;
        final String finalClassName = value;
        return List.of(new EntrypointMetadata() {
            @Override
            public String getAdapter() {
                return finalAdapter;
            }

            @Override
            public String getValue() {
                return finalClassName;
            }
        });
    }

    private static void parseLanguageAdapters(final Map<String, LanguageAdapter> languageAdapters, final JsonNode<?> node, final String modid) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassCastException {
        node.asTypeNodeOrThrow(JsonNode.NodeType.Map, "languageAdapters should be an object!");
        for (Pair<String, JsonNode<?>> pair : ((MapNode) node)) {
            String language = pair.getKey();
            String adapterClassName = (String) pair.getValue().asTypeNodeOrThrow(JsonNode.NodeType.String, "adapters should be a string!").getObj();
            Class<?> adapterClass = Class.forName(adapterClassName);
            LanguageAdapter adapter = (LanguageAdapter) adapterClass.getConstructor().newInstance();
            KessokuEntrypoint.registerLanguageAdapter(language, adapter);
            languageAdapters.put(language, adapter);
        }
    }
}
