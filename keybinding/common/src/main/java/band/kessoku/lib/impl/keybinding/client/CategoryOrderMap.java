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
package band.kessoku.lib.impl.keybinding.client;

import java.util.HashMap;
import java.util.Map;

import band.kessoku.lib.api.KessokuLib;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.text.Text;

// RawDiamondMC Note:
// Maybe some hacky changes to implement insertAfter and insertBefore?
public final class CategoryOrderMap extends HashMap<String, Integer> {
    private static CategoryOrderMap INSTANCE;

    private CategoryOrderMap(Map<String, Integer> map) {
        super(map);
        INSTANCE = this;
    }

    @ApiStatus.Internal
    public static CategoryOrderMap of(Map<String, Integer> map) {
        if (INSTANCE != null)
            throw new UnsupportedOperationException("CATEGORY_ORDER_MAP has already been initialized!");
        return new CategoryOrderMap(map);
    }

    @Override
    public Integer put(String key, Integer value) {
        if (this.containsValue(value) && value != Integer.MAX_VALUE && value != Integer.MIN_VALUE)
            KessokuLib.getLogger().warn(KessokuKeybinding.MARKER, "Duplicate category index found! Category: {} Index: {}", Text.translatable(key), value);
        return super.put(key, value);
    }

    public static CategoryOrderMap getInstance() {
        if (INSTANCE == null)
            throw new IllegalStateException("CATEGORY_ORDER_MAP has not been initialized yet!");
        return INSTANCE;
    }
}
