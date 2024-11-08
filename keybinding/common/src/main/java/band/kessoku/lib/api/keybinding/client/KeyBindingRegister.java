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

import band.kessoku.lib.api.base.KessokuUtils;
import band.kessoku.lib.impl.keybinding.client.CategoryOrderMap;
import band.kessoku.lib.service.keybinding.client.KeyBindingRegisterService;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public final class KeyBindingRegister {
    private KeyBindingRegister() {
    }

    public static boolean addCategory(String categoryTranslationKey) {
        Map<String, Integer> map = CategoryOrderMap.getInstance();

        if (map.containsKey(categoryTranslationKey)) {
            return false;
        }

        final int largest = map.values().stream().max(Integer::compareTo).orElse(0);
        map.put(categoryTranslationKey, largest + 1);
        return true;
    }

    public boolean hasCategory(String categoryTranslationKey) {
        return CategoryOrderMap.getInstance().containsKey(categoryTranslationKey);
    }

    public boolean hasCategory(int index) {
        return !this.getCategorySet(index).isEmpty();
    }

    public Set<String> getCategorySet(int index) {
        return KessokuUtils.getKeysByValue(CategoryOrderMap.getInstance(), index);
    }

    public @Nullable Integer indexOf(String categoryTranslationKey) {
        return CategoryOrderMap.getInstance().get(categoryTranslationKey);
    }

    public boolean addCategory(int index, String categoryTranslationKey) {
        if (this.hasCategory(categoryTranslationKey)) return false;
        CategoryOrderMap.getInstance().putIfAbsent(categoryTranslationKey, index);
        return true;
    }

    public static KeyBinding register(KeyBinding keyBinding) {
        return KeyBindingRegisterService.INSTANCE.register(keyBinding);
    }

    public static InputUtil.Key getBoundKey(KeyBinding keyBinding) {
        return KeyBindingRegisterService.INSTANCE.getBoundKey(keyBinding);
    }
}
