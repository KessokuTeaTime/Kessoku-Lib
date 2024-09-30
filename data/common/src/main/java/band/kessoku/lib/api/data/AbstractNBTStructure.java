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

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public abstract class AbstractNBTStructure extends AbstractDataStructure implements NBTSerializable {
    private final String id;

    public AbstractNBTStructure(String id) {
        this.id = id;
    }

    @Override
    public void write(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        NbtCompound root = new NbtCompound();
        for (Data<?> data : dataList) {
            if (data instanceof NBTSerializable serializable) {
                serializable.write(root, registries);
            }
        }
        for (DataStructure dataStructure : dataStructureList) {
            if (dataStructure instanceof NBTSerializable serializable) {
                serializable.write(root, registries);
            }
        }
        nbt.put(id, root);
    }

    @Override
    public void read(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        NbtCompound root = new NbtCompound();
        for (Data<?> data : dataList) {
            if (data instanceof NBTSerializable serializable) {
                serializable.read(root, registries);
            }
        }
        for (DataStructure dataStructure : dataStructureList) {
            if (dataStructure instanceof NBTSerializable serializable) {
                serializable.read(root, registries);
            }
        }
        nbt.put(id, root);
    }
}
