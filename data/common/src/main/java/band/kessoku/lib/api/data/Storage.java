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
package band.kessoku.lib.api.data;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public interface Storage<T> extends DataStructure {
    T get(int id);

    int size();

    static Storage<ItemStack> inventory(Inventory inventory) {
        return new Storage<>() {
            @Override
            public ItemStack get(int id) {
                return inventory.getStack(id);
            }

            @Override
            public int size() {
                return inventory.size();
            }
        };
    }
}
