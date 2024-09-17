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
package band.kessoku.lib.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.ServiceLoader;

public class ModUtils {
    public static <T> T loadService(Class<T> clazz) {
        return ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new AssertionError("No impl found for " + clazz.getName()));
    }

    public static Logger getLogger() {
        return LoggerFactory.getLogger("[KessokuLib]");
    }

    public static <T> boolean isType(List<?> list, Class<T> type) {
        for (Object element : list) {
            if (!(type.isInstance(element))) {
                return false;
            }
        }
        return true;
    }
    public static <V> V constructUnsafely(Class<V> cls) {
        try {
            Constructor<V> constructor = cls.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
