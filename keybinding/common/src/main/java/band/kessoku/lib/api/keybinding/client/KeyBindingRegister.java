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
package band.kessoku.lib.api.keybinding.client;

import band.kessoku.lib.service.keybinding.client.KeyBindingRegisterService;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public final class KeyBindingRegister {
    private KeyBindingRegister() {
    }

    /**
     * Registers the keybinding and add the keybinding category if required.
     *
     * @param keyBinding the keybinding
     * @return the keybinding itself
     * @throws IllegalArgumentException when a key binding with the same ID is already registered
     */
    public static KeyBinding registerKeyBinding(KeyBinding keyBinding) {
        return KeyBindingRegisterService.INSTANCE.register(keyBinding);
    }

    /**
     * Returns the configured KeyCode bound to the KeyBinding from the player's settings.
     *
     * @param keyBinding the keybinding
     * @return configured KeyCode
     */
    public static InputUtil.Key getBoundKeyOf(KeyBinding keyBinding) {
        return KeyBindingRegisterService.INSTANCE.getBoundKey(keyBinding);
    }
}
