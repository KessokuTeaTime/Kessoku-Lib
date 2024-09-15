package band.kessoku.lib.data.mixin;

import band.kessoku.lib.data.api.Data;
import band.kessoku.lib.data.api.NBTSerializable;
import band.kessoku.lib.data.api.DataStructure;
import band.kessoku.lib.data.api.BlockEntityStructure;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(BlockEntity.class)
public class BlockEntityMixin implements BlockEntityStructure {
    @Unique
    private final List<Data<?>> kessoku$dataList = new ArrayList<>();

    @Unique
    private final List<DataStructure> kessoku$DataStructureList = new ArrayList<>();

    @Override
    public <T> void integrate(Data<T> data) {
        kessoku$dataList.add(data);
    }

    @Override
    public void integrate(DataStructure dataStructure) {
        kessoku$DataStructureList.add(dataStructure);
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
