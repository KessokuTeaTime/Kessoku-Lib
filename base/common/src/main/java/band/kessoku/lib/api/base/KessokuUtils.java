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
package band.kessoku.lib.api.base;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

public final class KessokuUtils {
    private KessokuUtils() {
    }

    public static <T> boolean isType(final Collection<?> collection, final Class<T> type) {
        for (final Object element : collection) {
            if (!(type.isInstance(element))) {
                return false;
            }
        }
        return true;
    }

    public static <K, V> @NotNull Set<K> getKeysByValue(final Map<K, V> map, final V value) {
        final Set<K> result = new HashSet<>();
        map.forEach((k, v) -> {
            if (v.equals(value)) result.add(k);
        });
        return result;
    }
}
