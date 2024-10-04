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
package band.kessoku.lib.api;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import org.jetbrains.annotations.UnmodifiableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class KessokuLib {
    private static final List<Class<?>> initializedModules = new ArrayList<>();

    private KessokuLib() {
    }

    public static void loadModule(Class<?> moduleCommonClass) {
        // Try to get module name
        String moduleName;
        try {
            Field field = moduleCommonClass.getField("NAME");
            if (!Modifier.isStatic(field.getModifiers()))
                throw new IllegalArgumentException("NAME in " + moduleCommonClass.getPackageName() + " is not static!");
            field.setAccessible(true);
            moduleName = (String) field.get(null);
        } catch (NoSuchFieldException e) {
            getLogger().warn("no NAME found for {}! Using package name", moduleCommonClass.getPackageName());
            moduleName = moduleCommonClass.getPackageName();
        } catch (IllegalAccessException e) {
            // Already set accessible, shouldn't be called
            moduleName = moduleCommonClass.getPackageName();
        }
        initializedModules.add(moduleCommonClass);
        getLogger().info("{} loaded!", moduleName);
    }

    public static boolean isModuleLoaded(Class<?> moduleCommonClass) {
        return initializedModules.contains(moduleCommonClass);
    }

    @UnmodifiableView
    public static List<Class<?>> getActiveModules() {
        return Collections.unmodifiableList(initializedModules);
    }

    public static <T> T loadService(final Class<T> clazz) {
        return ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new AssertionError("No impl found for " + clazz.getPackageName()));
    }

    public static Logger getLogger() {
        return LoggerFactory.getLogger("[Kessoku Lib]");
    }
}
