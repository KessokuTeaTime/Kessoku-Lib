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
package band.kessoku.lib.impl.keybinding.neoforge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import band.kessoku.lib.mixin.keybinding.client.KeyBindingAccessor;
import band.kessoku.lib.service.keybinding.client.KeyBindingRegisterService;
import com.google.auto.service.AutoService;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@AutoService(KeyBindingRegisterService.class)
public final class KeyBindingRegisterImpl implements KeyBindingRegisterService {
    private static final List<KeyBinding> KEY_BINDINGS = new ReferenceArrayList<>();
    private static final GameOptions options = MinecraftClient.getInstance().options;
    private static boolean processed;

    @Override
    public KeyBinding register(KeyBinding keyBinding) {
        if (processed) {
            throw new IllegalStateException("Key bindings have already been processed");
        }

        var bindings = new ArrayList<>(List.of(options.allKeys));
        bindings.addAll(KEY_BINDINGS);

        for (KeyBinding existingKeyBindings : bindings) {
            if (existingKeyBindings == keyBinding) {
                throw new IllegalArgumentException("Attempted to register a key binding twice: " + keyBinding.getTranslationKey());
            } else if (existingKeyBindings.getTranslationKey().equals(keyBinding.getTranslationKey())) {
                throw new IllegalArgumentException("Attempted to register two key bindings with equal ID: " + keyBinding.getTranslationKey() + "!");
            }
        }

        // This will do nothing if the category already exists.
        addCategory(keyBinding.getCategory());
        KEY_BINDINGS.add(keyBinding);
        return keyBinding;
    }

    private static void addCategory(String categoryTranslationKey) {
        Map<String, Integer> map = KeyBindingAccessor.kessoku$getCategoryMap();
        final int largest = map.values().stream().max(Integer::compareTo).orElse(0);
        map.put(categoryTranslationKey, largest + 1);
    }

    @Override
    public InputUtil.Key getBoundKey(KeyBinding keyBinding) {
        return ((KeyBindingAccessor) keyBinding).kessoku$getBoundKey();
    }

    static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        KEY_BINDINGS.forEach(event::register);
        processed = true;
    }
}
