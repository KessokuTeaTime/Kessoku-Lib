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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

import band.kessoku.lib.api.KessokuLib;
import band.kessoku.lib.api.platform.Metadata;
import band.kessoku.lib.api.platform.Loader;
import band.kessoku.lib.api.entrypoint.entrypoints.KessokuPreLaunchEntrypoint;
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
    // Key, entry
    private static final Map<String, List<Entry>> entryMap = new HashMap<>();

    static {
        KessokuLib.getLogger().info(MARKER, "Start loading.");
        Map<String, KessokuMetadata> modInfoMap = new HashMap<>();
        for (Metadata modData : Loader.getMods()) {
            final String modid = modData.getModId();
            final Path kessokuJsonPath = modData.findPath("kessoku.json").orElse(null);
            // Not found
            if (kessokuJsonPath == null) continue;
            final MapNode json;
            try {
                json = (MapNode) JSON.json.parse(Files.readString(kessokuJsonPath)).asTypeNodeOrThrow(JsonNode.NodeType.Map, "Expect kessoku.json to be an object!");
            } catch (IOException e) {
                throw new RuntimeException("Failed to read kessoku.json from" + kessokuJsonPath, e);
            }
            // try to get schemaVersion
            if (!json.has("schemaVersion")) throw new NullPointerException("schemaVersion is required!");
            final int schemaVersion = (int) json.get("schemaVersion").asTypeNodeOrThrow(JsonNode.NodeType.Int, "schemaVersion should be an integer!").getObj();
            // Parse the json with correct version
            @SuppressWarnings("SwitchStatementWithTooFewBranches") final KessokuMetadata kessokuMetadata = switch (schemaVersion) {
                case 1 -> KessokuMetadata.parse(json, modid);
                default ->
                        throw new UnsupportedOperationException("Unsupported schemaVersion " + schemaVersion + " found! Consider updating Kessoku Lib to a newer version.");
            };
            modInfoMap.put(modid, kessokuMetadata);
        }
        // Init the modInfoMap
        KessokuEntrypoint.modInfoMap = Collections.unmodifiableMap(modInfoMap);
        // Init the java adapter
        adapters.put("java", JavaLanguageAdapter.INSTANCE);
        // Init the entryMap
        // forEach mods
        modInfoMap.forEach((modid, metadata) -> {
            // forEach mod entrypoints by key and entrypoint
            metadata.entrypoints().forEach((key, entrypointMetadataList) -> {
                // The entries of the key
                List<Entry> entries = new ArrayList<>();
                entrypointMetadataList.forEach(entrypointMetadata -> {
                            try {
                                Object instance = getAdapter(entrypointMetadata.getAdapter()).parse(Loader.getModMetadata(modid), entrypointMetadata.getValue());
                                entries.add(new Entry(modid, instance, entrypointMetadata.getValue()));
                            } catch (LanguageAdapterException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
                Objects.requireNonNull(entryMap.putIfAbsent(key, new ArrayList<>())).addAll(entries);
            });
        });
        invokeEntrypoint("prelaunch", KessokuPreLaunchEntrypoint.class, KessokuPreLaunchEntrypoint::onPreLaunch);
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

    public static <T> void invokeEntrypoint(String key, Class<T> type, Consumer<? super T> invoker) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(type);
        Objects.requireNonNull(invoker);
        Collection<RuntimeException> exceptions = new ArrayList<>();
        Collection<Entry> entries = getEntries(key);

        for (Entry entry : entries) {
            try {
                invoker.accept(entry.get(type));
            } catch (Throwable t) {
                exceptions.add(new RuntimeException(String.format(
                        "Could not execute entrypoint stage '%s' due to errors, provided by '%s' at '%s'!",
                        key, entry.metadata.getModId(), entry.definition
                )));
            }
        }

        if (!exceptions.isEmpty()) {
            RuntimeException exception = new RuntimeException("Failed to invoke '" + key + "' due to these errors: ");
            exception.setStackTrace(new StackTraceElement[0]);
            exceptions.forEach(exception::addSuppressed);
            throw exception;
        }
    }

    public static Collection<Entry> getEntries(String key) {
        if (hasEntrypoints(key))
            return Collections.unmodifiableList(entryMap.get(key));
        return List.of();
    }

    public static boolean hasEntrypoints(String key) {
        Objects.requireNonNull(key);
        return entryMap.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    public static final class Entry {
        public final Metadata metadata;
        private final Object instance;
        public final String definition;

        private Entry(String modid, Object instance, String definition) {
            this.metadata = Loader.getModMetadata(modid);
            this.instance = instance;
            this.definition = definition;
        }

        public <T> T get(Class<T> type) throws ClassCastException {
            return (T) this.instance;
        }
    }
}
