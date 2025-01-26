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

import static net.minecraft.block.entity.BlockEntity.tryParseCustomName;

import band.kessoku.lib.api.data.NBTSerializable;
import band.kessoku.lib.impl.data.BaseData;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;

public final class TextData extends BaseData<Text> implements NBTSerializable {
    private TextData(String id, Text defaultValue) {
        super(id, defaultValue);
    }

    public static TextData create(String id, Text defaultValue) {
        return new TextData(id, defaultValue);
    }

    @Override
    public void write(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        if (get() != null)
            nbt.putString(id(), Text.Serialization.toJsonString(get(), registries));
    }

    @Override
    public void read(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        Text text = tryParseCustomName(nbt.getString(id()), registries);
        if (text != null)
            set(text);
    }
}
