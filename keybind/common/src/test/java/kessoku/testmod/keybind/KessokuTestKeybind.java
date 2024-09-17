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
package kessoku.testmod.keybind;

import band.kessoku.lib.entrypoint.api.KessokuModInitializer;
import band.kessoku.lib.keybind.api.KeyBindRegister;
import net.minecraft.client.option.KeyBinding;

public class KessokuTestKeybind implements KessokuModInitializer {
    @Override
    public void onInitialize() {
        KeyBindRegister.getInstance().register(
                new KeyBinding(
                "Test Name",
                0,
                "Test Category"
                )
        );
    }
}
