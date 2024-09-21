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
package band.kessoku.lib.entrypoint;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import band.kessoku.lib.base.ModUtils;
import band.kessoku.lib.entrypoint.api.LanguageAdapter;
import band.kessoku.lib.entrypoint.impl.EntrypointMetadata;
import band.kessoku.lib.entrypoint.impl.JavaLanguageAdapter;
import band.kessoku.lib.entrypoint.impl.KessokuMetadata;
import band.kessoku.lib.entrypoint.impl.exceptions.LanguageAdapterException;
import band.kessoku.lib.platform.api.ModData;
import band.kessoku.lib.platform.api.ModLoader;
import club.someoneice.json.JSON;
import club.someoneice.json.node.JsonNode;
import club.someoneice.json.node.MapNode;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public final class KessokuEntrypoint {
    private KessokuEntrypoint() {
    }

    public static final String MOD_ID = "kessoku_entrypoint";
    public static final Marker MARKER = MarkerFactory.getMarker("[KessokuEntrypoint]");
    public static final int LATEST_SCHEMA_VERSION = 1;
    private static Map<String, KessokuMetadata> modInfoMap;
    private static final Map<String, LanguageAdapter> adapters = new HashMap<>();
    private static final Map<String, List<Entry>> entryMap = new HashMap<>();

    static void init() {
        if (modInfoMap != null) throw new IllegalStateException("Kessoku Entrypoint API is already loaded!");
        ModUtils.getLogger().info(MARKER, "Start loading Kessoku Entrypoint API.");
        Map<String, KessokuMetadata> map = new HashMap<>();
        for (ModData modData : ModLoader.getMods()) {
            final String modid = modData.getModId();
            final Path path = modData.findPath("kessoku.json").orElse(null);
            if (path == null) continue;
            try {
                final JsonNode<?> raw = JSON.json.parse(Files.readString(path));
                if (!raw.typeOf(JsonNode.NodeType.Map)) throw new IllegalArgumentException("obj!");
                final MapNode json = (MapNode) raw;
                if (!json.has("schemaVersion")) throw new NullPointerException("schemaVersion is required!");
                if (!json.get("schemaVersion").typeOf(JsonNode.NodeType.Int))
                    throw new IllegalArgumentException("schemaVersion should be an integer!");
                final int schemaVersion = (int) json.get("schemaVersion").getObj();
                final KessokuMetadata info = switch (schemaVersion) {
                    case 1 -> KessokuMetadata.parse(json, modid);
                    default ->
                            throw new IllegalArgumentException("unsupported schemaVersion " + schemaVersion + " found!");
                };
                map.put(modid, info);
            } catch (Exception e) {
                throw new RuntimeException("Failed to read kessoku info file from mod " + modData.getModId(), e);
            }
        }
        modInfoMap = Collections.unmodifiableMap(map);
        adapters.put("java", JavaLanguageAdapter.INSTANCE);
        map.forEach((modid, metadata) ->
                metadata.entrypoints().forEach((key, entrypointMetadataList) -> {
                    List<Entry> entries = new ArrayList<>();
                    entrypointMetadataList.forEach(entrypointMetadata -> {
                                try {
                                    Object instance = getAdapter(entrypointMetadata.getAdapter()).parse(ModLoader.getModData(modid), entrypointMetadata.getValue());
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
        if (adapters.containsKey(language)) throw new IllegalStateException(language + " is already been registered!");
        adapters.putIfAbsent(language, adapter);
    }

    public static LanguageAdapter getAdapter(String language) {
        return adapters.get(language);
    }

    @SuppressWarnings("unchecked")
    private static final class Entry {
        public final ModData modData;
        private final Object instance;

        public Entry(String modid, Object instance) {
            this.modData = ModLoader.getModData(modid);
            this.instance = instance;
        }

        public <T> T get(Class<T> type) {
            return (T) this.instance;
        }
    }
}
