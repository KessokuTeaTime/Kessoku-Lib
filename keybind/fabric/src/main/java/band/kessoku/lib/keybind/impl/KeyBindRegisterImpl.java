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
package band.kessoku.lib.keybind.impl;

import band.kessoku.lib.keybind.api.KeyBindRegister;
import com.google.auto.service.AutoService;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

@AutoService(KeyBindRegister.class)
public class KeyBindRegisterImpl implements KeyBindRegister {
    @Override
    public boolean addCategory(String categoryTranslationKey) {
        return KeyBindingRegistryImpl.addCategory(categoryTranslationKey);
    }

    @Override
    public KeyBinding register(KeyBinding keyBinding) {
        return KeyBindingHelper.registerKeyBinding(keyBinding);
    }

    @Override
    public InputUtil.Key getBoundKey(KeyBinding keyBinding) {
        return KeyBindingHelper.getBoundKeyOf(keyBinding);
    }
}
