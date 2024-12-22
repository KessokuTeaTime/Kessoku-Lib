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
package band.kessoku.lib.impl.config.neoforge;

import band.kessoku.lib.config.ConfigHandler;
import band.kessoku.lib.config.KessokuConfig;
import band.kessoku.lib.config.api.Config;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import org.apache.commons.compress.utils.Lists;

import java.lang.annotation.ElementType;
import java.util.List;
import java.util.Objects;

@Mod(KessokuConfig.MOD_ID)
public final class KessokuConfigNeoforge {
    public KessokuConfigNeoforge() {
        List<Class<?>> configClazz = Lists.newArrayList();
        FMLLoader.getLoadingModList().getMods().forEach(modInfo -> {
            modInfo.getOwningFile().getFile().compileContent().getAnnotatedBy(Config.class, ElementType.TYPE)
                    .map(it -> it.clazz().getClassName())
                    .map(KessokuConfigNeoforge::getClassByName)
                    .filter(Objects::nonNull)
                    .forEach(configClazz::add);
        });

        ConfigHandler.handleConfigs(configClazz, FMLPaths.CONFIGDIR.get());
    }

    private static Class<?> getClassByName(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
