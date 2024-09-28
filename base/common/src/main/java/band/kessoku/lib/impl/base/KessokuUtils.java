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
package band.kessoku.lib.impl.base;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.*;

public final class KessokuUtils {
    private KessokuUtils() {
    }

    public static <T> T loadService(final Class<T> clazz) {
        return ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new AssertionError("No impl found for " + clazz.getPackageName()));
    }

    public static Logger getLogger() {
        return LoggerFactory.getLogger("[Kessoku Lib]");
    }

    public static <T> boolean isType(final List<?> list, final Class<T> type) {
        for (final Object element : list) {
            if (!(type.isInstance(element))) {
                return false;
            }
        }
        return true;
    }

    public static <V> V constructUnsafely(final Class<V> cls) {
        try {
            final Constructor<V> constructor = cls.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static <K, V> @NotNull Set<K> getKeysByValue(final Map<K, V> map, final V value) {
        final Set<K> result = new HashSet<>();
        map.forEach((k, v) -> {
            if (v.equals(value)) result.add(k);
        });
        return result;
    }
}
