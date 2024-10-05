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
package band.kessoku.lib.api.entrypoint;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import band.kessoku.lib.api.KessokuLib;
import band.kessoku.lib.api.platform.Metadata;
import band.kessoku.lib.api.platform.Loader;
import band.kessoku.lib.impl.entrypoint.JavaLanguageAdapter;
import band.kessoku.lib.impl.entrypoint.exceptions.LanguageAdapterException;
import club.someoneice.json.JSON;
import club.someoneice.json.node.JsonNode;
import club.someoneice.json.node.MapNode;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public final class KessokuEntrypoint {
    private KessokuEntrypoint() {
    }

    public static final String MOD_ID = "kessoku_entrypoint";
    public static final String NAME = "Kessoku Entrypoint API";
    public static final Marker MARKER = MarkerFactory.getMarker("[" + NAME + "]");
    public static final int LATEST_SCHEMA_VERSION = 1;
    private static Map<String, KessokuMetadata> modInfoMap;
    private static final Map<String, LanguageAdapter> adapters = new HashMap<>();
    private static final Map<String, List<Entry>> entryMap = new HashMap<>();

    static {
        KessokuLib.getLogger().info(MARKER, "Start loading Kessoku Entrypoint API.");
        Map<String, KessokuMetadata> modInfoMap = new HashMap<>();
        for (Metadata modData : Loader.getMods()) {
            final String modid = modData.getModId();
            final Path kessokuJsonPath = modData.findPath("kessoku.json").orElse(null);
            // Not found
            if (kessokuJsonPath == null) continue;
            try {
                final MapNode json = (MapNode) JSON.json.parse(Files.readString(kessokuJsonPath)).asTypeNodeOrThrow(JsonNode.NodeType.Map, "Expect kessoku.json to be an object!");
                if (!json.has("schemaVersion")) throw new NullPointerException("schemaVersion is required!");
                final int schemaVersion = (int) json.get("schemaVersion").asTypeNodeOrThrow(JsonNode.NodeType.Int, "schemaVersion should be an integer!").getObj();
                @SuppressWarnings("SwitchStatementWithTooFewBranches") final KessokuMetadata metadata = switch (schemaVersion) {
                    case 1 -> KessokuMetadata.parse(json, modid);
                    default ->
                            throw new UnsupportedOperationException("Unsupported schemaVersion " + schemaVersion + " found! Consider updating Kessoku Lib to a newer version.");
                };
                modInfoMap.put(modid, metadata);
            } catch (Exception e) {
                throw new RuntimeException("Failed to read kessoku info file from mod " + modData.getModId(), e);
            }
        }
        KessokuEntrypoint.modInfoMap = Collections.unmodifiableMap(modInfoMap);
        adapters.put("java", JavaLanguageAdapter.INSTANCE);
        modInfoMap.forEach((modid, metadata) ->
                metadata.entrypoints().forEach((key, entrypointMetadataList) -> {
                    List<Entry> entries = new ArrayList<>();
                    entrypointMetadataList.forEach(entrypointMetadata -> {
                                try {
                                    Object instance = getAdapter(entrypointMetadata.getAdapter()).parse(Loader.getModMetadata(modid), entrypointMetadata.getValue());
                                    entries.add(new Entry(modid, instance));
                                } catch (LanguageAdapterException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    );
                    Objects.requireNonNull(entryMap.putIfAbsent(key, new ArrayList<>())).addAll(entries);
                })
        );
    }

    public static <T extends LanguageAdapter> void registerLanguageAdapter(String language, T adapter) {
        if (adapters.containsKey(language)) throw new IllegalStateException(language + " has already been registered!");
        adapters.putIfAbsent(language, adapter);
    }

    public static LanguageAdapter getAdapter(String language) {
        return adapters.get(language);
    }

    public static Optional<KessokuMetadata> getKessokuMetadata(String modid) {
        return Optional.ofNullable(modInfoMap.get(modid));
    }

    @SuppressWarnings("unchecked")
    private static final class Entry {
        public final Metadata metadata;
        private final Object instance;

        public Entry(String modid, Object instance) {
            this.metadata = Loader.getModMetadata(modid);
            this.instance = instance;
        }

        public <T> T get(Class<T> type) {
            return (T) this.instance;
        }
    }
}
