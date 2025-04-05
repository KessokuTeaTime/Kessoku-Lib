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

import java.util.*;
import java.util.stream.Collectors;

import band.kessoku.lib.impl.entrypoint.exceptions.KessokuParseException;
import club.someoneice.json.Pair;
import club.someoneice.json.node.ArrayNode;
import club.someoneice.json.node.JsonNode;
import club.someoneice.json.node.MapNode;
import com.google.common.collect.Maps;

public record KessokuMetadata(Map<String, List<EntrypointMetadata>> entrypoints,
                              Map<String, LanguageAdapter> languageAdapters, String modid) {

    public KessokuMetadata(final Map<String, List<EntrypointMetadata>> entrypoints,
                           final Map<String, LanguageAdapter> languageAdapters,
                           final String modid) {
        this.entrypoints = Collections.unmodifiableMap(entrypoints);
        this.languageAdapters = Collections.unmodifiableMap(languageAdapters);
        this.modid = modid;
    }

    public static KessokuMetadata parse(final MapNode json, final String modid) {
        final Map<String, List<EntrypointMetadata>> entrypoints = Maps.newHashMap();
        final Map<String, LanguageAdapter> languageAdapters = Maps.newHashMap();

        try {
            final JsonNode<?> nodeEntrypoint = json.get("entrypoint");
            final JsonNode<?> nodeLanguageAdapters = json.get("languageAdapters");

            Objects.requireNonNull(nodeEntrypoint, "Can't found the meta: entrypoint");
            Objects.requireNonNull(nodeLanguageAdapters, "Can't found the meta: languageAdapters");

            parseEntrypoint(entrypoints, nodeEntrypoint);
            parseLanguageAdapters(languageAdapters, nodeLanguageAdapters);

            return new KessokuMetadata(entrypoints, languageAdapters, modid);
        } catch (Exception e) {
            throw new KessokuParseException(e, "Failed to parse kessoku.json provided by %s".formatted(modid));
        }
    }

    private static void parseEntrypoint(final Map<String, List<EntrypointMetadata>> entrypoints,
                                        final JsonNode<?> node) {
        // Check if it's an object
        final MapNode mapNode = (MapNode) node.asTypeNodeOrThrow(JsonNode.NodeType.Map,
                "entrypoint should be an object!");

        mapNode.forEach(pair -> {
            // normalize value
            final JsonNode<?> rawValue = pair.getValue();
            final List<EntrypointMetadata> entrypointMetadataList = switch (rawValue.getType()) {
                case Map -> parseObjectEntrypoint((MapNode) rawValue.asTypeNode());
                case Array -> parseArrayEntrypoint((ArrayNode) rawValue.asTypeNode());
                case String -> createData("java", rawValue.toString());
                default -> throw new IllegalArgumentException("Entrypoint should be a object, string or array, but it's %s!"
                        .formatted(rawValue.getType().toString().toLowerCase()));
            };
            entrypoints.put(pair.getKey(), entrypointMetadataList);
        });
    }

    private static List<EntrypointMetadata> parseObjectEntrypoint(final MapNode node) {
        final JsonNode<?> nodeAdapter = node.get("adapter");
        final JsonNode<?> nodeValue = node.get("value");

        Objects.requireNonNull(nodeValue, "Value of class name cannot be null!");

        final String adapter = Objects.nonNull(nodeAdapter)
                ? nodeAdapter.asTypeNodeOrThrow(JsonNode.NodeType.String, "Adapter should be a string").toString()
                : "java";
        final String value = nodeValue.asTypeNodeOrThrow(JsonNode.NodeType.String, "Value should be a string").toString();

        return createData(adapter, value);
    }

    private static List<EntrypointMetadata> parseArrayEntrypoint(final ArrayNode node) {
        return node.isEmpty()
                ? List.of()
                : switch (node.get(0).getType())
        {
            case String -> node.stream()
                        .map(it -> createData("java", it.toString()))
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

            case Map -> node.stream()
                        .map(it -> (MapNode) it.asTypeNodeOrThrow(JsonNode.NodeType.Map))
                        .map(KessokuMetadata::parseObjectEntrypoint)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());

            default ->
                    throw new IllegalArgumentException("Entrypoint should be an array of string or map, but it's %s!"
                            .formatted(node.get(0).getType().toString().toLowerCase()));
        };
    }

    private static void parseLanguageAdapters(final Map<String, LanguageAdapter> languageAdapters,
                                              final JsonNode<?> node) throws ReflectiveOperationException {
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

    private static List<EntrypointMetadata> createData(final String adapter, final String value) {
        return List.of(new EntrypointMetadata() {
            @Override
            public String getAdapter() {
                return adapter;
            }

            @Override
            public String getValue() {
                return value;
            }
        });
    }
}
