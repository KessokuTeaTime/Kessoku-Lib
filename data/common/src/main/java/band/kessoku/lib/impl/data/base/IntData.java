package band.kessoku.lib.impl.data.base;

import band.kessoku.lib.api.data.NBTSerializable;
import band.kessoku.lib.impl.data.BaseData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public final class IntData extends BaseData<Integer> implements NBTSerializable {
    private IntData(String id, int defaultValue) {
        super(id, defaultValue);
    }

    public static IntData create(String id, int defaultValue) {
        return new IntData(id, defaultValue);
    }

    @Override
    public void write(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        nbt.putInt(id(), get());
    }

    @Override
    public void read(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        set(nbt.getInt(id()));
    }
}
