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
package band.kessoku.lib.api.base.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Arrays;

import band.kessoku.lib.api.KessokuLib;

public final class ReflectUtil {
    private ReflectUtil() {
    }

    public static boolean isAssignableFrom(Field field, Class<?>... classes) {
        var flag = Arrays.stream(classes).anyMatch(clazz -> !field.getType().isAssignableFrom(clazz));
        return !flag;
    }

    public static boolean isAssignableFrom(Object o, Class<?>... classes) {
        var flag = Arrays.stream(classes).anyMatch(clazz -> !o.getClass().isAssignableFrom(clazz));
        return !flag;
    }

    public static boolean markAccessible(AccessibleObject obj) {
        try {
            obj.setAccessible(true);
            return true;
        } catch (Exception e) {
            KessokuLib.getLogger().error(e.getMessage(), e);
            return false;
        }
    }
}
