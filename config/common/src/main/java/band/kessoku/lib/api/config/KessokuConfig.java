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
package band.kessoku.lib.api.config;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class KessokuConfig {
    private static final Map<AbstractConfig, Class<ConfigSerializer>> configs = new HashMap<>();
    private static final Map<Class<? extends ConfigSerializer>, ConfigSerializer> serializerCache = new HashMap<>();

    public static final String MOD_ID = "kessoku_config";
    public static final String NAME = "Kessoku Config API";
    public static final Marker MARKER = MarkerFactory.getMarker("[" + NAME +"]");

    @SuppressWarnings({"unchecked", "unused"})
    public static <T extends AbstractConfig, S extends ConfigSerializer> T register(T config, Class<S> serializer) {
        if (config.getClass().isAnonymousClass()) throw new IllegalArgumentException();

        try {
            FileUtils.touch(config.getPath().toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        configs.put(config, (Class<ConfigSerializer>) serializer);
        return config;
    }

    @Contract(pure = true)
    @Nullable
    public static <T extends AbstractConfig> Class<ConfigSerializer> getSerializer(T config) {
        return configs.get(config);
    }

    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    @NotNull
    public static <T extends ConfigSerializer> T getSerializerInstance(Class<T> serializer) {
        try {
            return (T) Objects.requireNonNull(serializerCache.containsKey(serializer)
                    ? serializerCache.get(serializer)
                    : serializerCache.put(serializer,
                        serializer.getConstructor().newInstance()));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
