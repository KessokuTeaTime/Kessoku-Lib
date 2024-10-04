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
package band.kessoku.lib.impl.keybinding.neoforge;

import band.kessoku.lib.api.KessokuLib;
import band.kessoku.lib.impl.keybinding.client.KessokuKeybinding;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;

@Mod(value = KessokuKeybinding.MOD_ID, dist = Dist.CLIENT)
public final class KessokuKeyBindingNeoforge {
    public KessokuKeyBindingNeoforge(IEventBus modEventBus) {
        if (FMLLoader.getDist().isClient()) {
            KessokuLib.loadModule(KessokuKeybinding.class);
            KeyBindingRegisterImpl.registerEvent(modEventBus);
        }
    }
}
