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
package band.kessoku.lib.mixins.data;

import java.util.ArrayList;
import java.util.List;

import band.kessoku.lib.api.data.BlockEntityStructure;
import band.kessoku.lib.api.data.Data;
import band.kessoku.lib.api.data.DataStructure;
import band.kessoku.lib.api.data.NBTSerializable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(BlockEntity.class)
public class BlockEntityMixin implements BlockEntityStructure {
    @Unique
    private final List<Data<?>> kessoku$dataList = new ArrayList<>();

    @Unique
    private final List<DataStructure> kessoku$DataStructureList = new ArrayList<>();

    @Override
    public <T, K extends Data<T>> K integrate(K data) {
        kessoku$dataList.add(data);
        return data;
    }

    @Override
    public DataStructure integrate(DataStructure dataStructure) {
        kessoku$DataStructureList.add(dataStructure);
        return dataStructure;
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    private void readNbtMixin(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup, CallbackInfo ci) {
        for (Data<?> data : kessoku$dataList) {
            if (data instanceof NBTSerializable serializable) {
                serializable.read(nbt, registryLookup);
            }
        }
        for (DataStructure dataStructure : kessoku$DataStructureList) {
            if (dataStructure instanceof NBTSerializable serializable) {
                serializable.read(nbt, registryLookup);
            }
        }
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    private void writeNbtMixin(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup, CallbackInfo ci) {
        for (Data<?> data : kessoku$dataList) {
            if (data instanceof NBTSerializable serializable) {
                serializable.write(nbt, registryLookup);
            }
        }
        for (DataStructure dataStructure : kessoku$DataStructureList) {
            if (dataStructure instanceof NBTSerializable serializable) {
                serializable.write(nbt, registryLookup);
            }
        }
    }
}
