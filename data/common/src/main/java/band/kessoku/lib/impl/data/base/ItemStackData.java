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
package band.kessoku.lib.impl.data.base;

import band.kessoku.lib.api.data.NBTSerializable;
import band.kessoku.lib.impl.data.BaseData;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public final class ItemStackData extends BaseData<ItemStack> implements NBTSerializable {
    private ItemStackData(String id, ItemStack defaultValue) {
        super(id, defaultValue);
    }

    public static ItemStackData create(String id, ItemStack defaultValue) {
        return new ItemStackData(id, defaultValue);
    }

    @Override
    public void write(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        if (!get().isEmpty()) {
            NbtCompound itemNBT = new NbtCompound();
            get().encode(registries, itemNBT);
            nbt.put(id(), itemNBT);
        }
    }

    @Override
    public void read(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        set(ItemStack.fromNbt(registries, nbt.getCompound(id())).orElse(ItemStack.EMPTY));
    }
}
