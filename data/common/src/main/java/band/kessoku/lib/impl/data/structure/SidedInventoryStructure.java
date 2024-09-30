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
package band.kessoku.lib.impl.data.structure;

import java.util.ArrayList;
import java.util.List;

import band.kessoku.lib.impl.data.collection.Access;
import org.jetbrains.annotations.Nullable;

import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public final class SidedInventoryStructure extends InventoryStructure implements SidedInventory {
    private final List<Access> list = new ArrayList<>();

    public SidedInventoryStructure(String id, int size) {
        super(id, size);
    }

    @Override
    public Access element(String id, int index) {
        Access access = Access.create(id, index, this.inventory);
        list.add(access);
        return access;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return list.stream()
                .filter(access -> access.input.test(side) || access.output.test(side))
                .mapToInt(access -> access.index)
                .toArray();
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return list.stream()
                .filter(access -> access.index == slot)
                .findFirst()
                .map(access -> access.input.test(dir) && access.itemstack.test(stack))
                .orElse(false);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return list.stream()
                .filter(access -> access.index == slot)
                .findFirst()
                .map(access -> access.output.test(dir) && access.itemstack.test(stack))
                .orElse(false);
    }

}
