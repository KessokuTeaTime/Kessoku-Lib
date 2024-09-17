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

import band.kessoku.lib.event.util.NeoEventUtils;
import band.kessoku.lib.keybind.api.KeyBindRegister;
import band.kessoku.lib.keybind.mixin.KeyBindingAccessor;
import com.google.auto.service.AutoService;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AutoService(KeyBindRegister.class)
public class KeyBindRegisterImpl implements KeyBindRegister {
    private static final List<KeyBinding> KEY_BINDINGS = new ReferenceArrayList<>();
    private static boolean processed;

    private static Map<String, Integer> getCategoryMap() {
        return KeyBindingAccessor.kessoku$getCategoryMap();
    }

    @Override
    public boolean addCategory(String categoryTranslationKey) {
        Map<String, Integer> map = getCategoryMap();

        if (map.containsKey(categoryTranslationKey)) {
            return false;
        }

        Optional<Integer> largest = map.values().stream().max(Integer::compareTo);
        int largestInt = largest.orElse(0);
        map.put(categoryTranslationKey, largestInt + 1);
        return true;
    }

    @Override
    public KeyBinding register(KeyBinding keyBinding) {
        if (processed) {
            throw new IllegalStateException("Key bindings have already been processed");
        }

        for (KeyBinding existingKeyBindings : KEY_BINDINGS) {
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

    @Override
    public InputUtil.Key getBoundKey(KeyBinding keyBinding) {
        return ((KeyBindingAccessor) keyBinding).kessoku$getBoundKey();
    }

    public static void registerEvent(IEventBus eventBus) {
        NeoEventUtils.registerEvent(eventBus, RegisterKeyMappingsEvent.class, event -> {
            KEY_BINDINGS.forEach(event::register);
            processed = true;
        });
    }
}
