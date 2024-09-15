package band.kessoku.lib.data.impl;

import band.kessoku.lib.data.api.Data;
import band.kessoku.lib.data.api.MutableData;
import band.kessoku.lib.data.api.NBTData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public class LongData extends BaseData<Long> implements NBTData<Long> {
    private LongData(String id, long defaultValue) {
        super(id, defaultValue);
    }

    public static MutableData<Long> mutable(String id, long defaultValue) {
        return new LongData(id, defaultValue);
    }

    public static Data<Long> immutable(String id, long defaultValue) {
        return new LongData(id, defaultValue);
    }

    public static NBTData<Long> nbt(String id, long defaultValue) {
        return new LongData(id, defaultValue);
    }

    @Override
    public void write(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.putLong(id(), get());
    }

    @Override
    public void read(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        set(nbt.getLong(id()));
    }
}
