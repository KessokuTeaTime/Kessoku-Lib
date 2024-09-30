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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import band.kessoku.lib.impl.base.KessokuUtils;
import org.jetbrains.annotations.UnmodifiableView;

public final class KessokuLib {
    private static final List<Class<?>> initializedModules = new ArrayList<>();

    private KessokuLib() {
    }

    public static void loadModule(Class<?> moduleCommonClass, Object... args) {
        // Try to get module name
        String moduleName;
        try {
            Field field = moduleCommonClass.getField("NAME");
            if (!Modifier.isStatic(field.getModifiers()))
                throw new IllegalArgumentException("NAME in " + moduleCommonClass.getPackageName() + " is not static!");
            field.setAccessible(true);
            moduleName = (String) field.get(null);
        } catch (NoSuchFieldException e) {
            KessokuUtils.getLogger().warn("no NAME found for {}! Using package name", moduleCommonClass.getPackageName());
            moduleName = moduleCommonClass.getPackageName();
        } catch (IllegalAccessException e) {
            // Already set accessible, shouldn't be called
            moduleName = moduleCommonClass.getPackageName();
        }

        // Modules shouldn't be able to load for multiple times
        if (isModuleLoaded(moduleCommonClass)) {
            throw new UnsupportedOperationException(moduleName + " is already loaded!");
        }

        // Get init method
        Class<?>[] argClasses = (Class<?>[]) Arrays.stream(args).map(Object::getClass).toArray();
        Method method;
        try {
            method = moduleCommonClass.getMethod("init", argClasses);
        } catch (NoSuchMethodException e) {
            // The module doesn't need to be initialized
            initializedModules.add(moduleCommonClass);
            KessokuUtils.getLogger().info("{} loaded!", moduleName);
            return;
        }

        // init method should be static
        if (!Modifier.isStatic(method.getModifiers())) {
            KessokuUtils.getLogger().error("init method of {} is not static!", moduleName);
            return;
        }

        method.setAccessible(true);

        // initialize module
        try {
            method.invoke(null, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to initialize Kessoku Module " + moduleName + " !");
        }
        initializedModules.add(moduleCommonClass);
        KessokuUtils.getLogger().info("{} loaded!", moduleName);
    }

    public static boolean isModuleLoaded(Class<?> moduleCommonClass) {
        return initializedModules.contains(moduleCommonClass);
    }

    @UnmodifiableView
    public static List<Class<?>> getActiveModules() {
        return Collections.unmodifiableList(initializedModules);
    }
}
