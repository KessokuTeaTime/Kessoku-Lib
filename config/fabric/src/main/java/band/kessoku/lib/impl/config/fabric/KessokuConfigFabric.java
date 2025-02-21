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
package band.kessoku.lib.impl.config.fabric;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

import band.kessoku.lib.api.KessokuLib;
import band.kessoku.lib.api.config.Config;
import band.kessoku.lib.api.config.core.ConfigHandler;
import org.apache.commons.compress.utils.Lists;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public final class KessokuConfigFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        List<Class<?>> configClazz = Lists.newArrayList();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources;
        try {
            resources = classLoader.getResources("");
        } catch (IOException e) {
            KessokuLib.getLogger().error(e.getMessage(), e);
            return;
        }
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File directory = new File(resource.getFile());
            File[] files = directory.listFiles();
            for (File file : files) {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isAnnotationPresent(Config.class)) {
                        configClazz.add(clazz);
                    }
                } catch (Exception e) {
                    KessokuLib.getLogger().error(e.getMessage(), e);
                }
            }
        }

        ConfigHandler.handleConfigs(configClazz, FabricLoader.getInstance().getConfigDir());
    }
}
