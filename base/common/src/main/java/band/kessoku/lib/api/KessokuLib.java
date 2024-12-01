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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

import org.apache.logging.log4j.core.util.ReflectionUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class KessokuLib {
    private static final List<Class<?>> initializedModules = new ArrayList<>();

    private KessokuLib() {
    }

    public static void loadModule(@NotNull Class<?> moduleCommonClass) {
        if (moduleCommonClass.isArray())
            throw new UnsupportedOperationException("What the hell are you doing?? KessokuLib.loadModule receives an array class! " + moduleCommonClass.getName());
        if (isModuleLoaded(moduleCommonClass)) {
            throw new UnsupportedOperationException("Module `" + moduleCommonClass.getName() + "` has already been loaded!");
        }
        // Try to get module name
        String moduleName;
        try {
            final Field field = moduleCommonClass.getField("NAME");
            moduleName = (String) ReflectionUtil.getStaticFieldValue(field);
        } catch (NoSuchFieldException e) {
            moduleName = moduleCommonClass.getName();
            getLogger().warn("Field `NAME` is not found in {}! Using fully qualified class name.", moduleName);
        } catch (NullPointerException e) {
            moduleName = moduleCommonClass.getName();
            getLogger().warn("NAME in {} is not static! Using package name.", moduleName);
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
