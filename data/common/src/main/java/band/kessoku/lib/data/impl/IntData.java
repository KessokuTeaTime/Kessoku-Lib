package band.kessoku.lib.data.impl;

import band.kessoku.lib.data.api.Data;
import band.kessoku.lib.data.api.MutableData;
import band.kessoku.lib.data.api.NBTData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public class IntData extends BaseData<Integer> implements NBTData<Integer> {
    private IntData(String id, int defaultValue) {
        super(id, defaultValue);
    }

    public static MutableData<Integer> mutable(String id, int defaultValue) {
        return new IntData(id, defaultValue);
    }

    public static Data<Integer> immutable(String id, int defaultValue) {
        return new IntData(id, defaultValue);
    }

    public static NBTData<Integer> nbt(String id, int defaultValue) {
        return new IntData(id, defaultValue);
    }

    @Override
    public void write(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.putInt(id(), get());
    }

    @Override
    public void read(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        set(nbt.getInt(id()));
    }
}
