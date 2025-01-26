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
package band.kessoku.lib.api;

import java.lang.reflect.Field;
import java.util.ServiceLoader;

import org.apache.logging.log4j.core.util.ReflectionUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class KessokuLib {
    private KessokuLib() {
    }

    public static void loadModule(@NotNull final Class<?> moduleCommonClass) {
        if (moduleCommonClass.isArray())
            throw new UnsupportedOperationException("Cannot load an Array! Received " + moduleCommonClass.getName());
        // Try to get module name
        final String moduleName;
        try {
            final Field field = moduleCommonClass.getField("NAME");
            moduleName = (String) ReflectionUtil.getStaticFieldValue(field);
        } catch (NoSuchFieldException e) {
            getLogger().error("Invalid Kessoku module! Field `NAME` is not found in {}!", moduleCommonClass.getName());
            return;
        } catch (NullPointerException e) {
            getLogger().error("Invalid Kessoku module! NAME in {} is not static!", moduleCommonClass.getName());
            return;
        }
        getLogger().info("{} loaded!", moduleName);
    }

    public static <T> T loadService(final Class<T> clazz) {
        return ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new AssertionError("No impl found for " + clazz.getPackageName()));
    }

    public static Logger getLogger() {
        return LoggerFactory.getLogger("[Kessoku Lib]");
    }
}
