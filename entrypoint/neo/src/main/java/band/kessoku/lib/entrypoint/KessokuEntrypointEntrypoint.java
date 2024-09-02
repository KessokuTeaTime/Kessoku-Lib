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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import band.kessoku.lib.base.ModUtils;
import band.kessoku.lib.entrypoint.api.KessokuClientModInitializer;
import band.kessoku.lib.entrypoint.api.KessokuDedicatedServerModInitializer;
import band.kessoku.lib.entrypoint.api.KessokuModInitializer;
import com.google.common.collect.ImmutableList;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;

@Mod(KessokuEntrypoint.MOD_ID)
public final class KessokuEntrypointEntrypoint {
    private static final Map<String, ImmutableList<?>> entrypointMap = new HashMap<>();

    public KessokuEntrypointEntrypoint(Dist dist) {
        ModUtils.getLogger().info(KessokuEntrypoint.MARKER, "KessokuLib-Entrypoint is loaded!");
        registerEntrypoint("kMain", KessokuModInitializer.class);
        registerEntrypoint("kClient", KessokuClientModInitializer.class);
        registerEntrypoint("kServer", KessokuDedicatedServerModInitializer.class);
    }

    @SuppressWarnings("unchecked")
    public static  <T> void registerEntrypoint(String entrypointName, Class<T> initializer) {
        if (entrypointMap.containsKey(entrypointName)) throw new UnsupportedOperationException();
        final ImmutableList.Builder<T> builder = ImmutableList.builder();
        ModList.get().getMods().forEach(info -> {
            try {
                Map<String, Object> properties = info.getModProperties();
                if (properties.containsKey(entrypointName)) {
                    Object entrypointList = properties.get(entrypointName);
                    if (!(entrypointList instanceof List)) throw new IllegalArgumentException();
                    if (!ModUtils.isType((List<?>) entrypointList, String.class)) {
                        for (Object entrypoint : ((List<?>) entrypointList)) {
                            Class<?> clazz = Class.forName((String) entrypoint);
                            if (!clazz.isAssignableFrom(initializer)) throw new IllegalArgumentException();
                            builder.add((T) clazz.getConstructor().newInstance());
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        entrypointMap.put(entrypointName, builder.build());
    }
}
