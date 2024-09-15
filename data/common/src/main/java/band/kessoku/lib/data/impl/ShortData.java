package band.kessoku.lib.data.impl;

import band.kessoku.lib.data.api.Data;
import band.kessoku.lib.data.api.MutableData;
import band.kessoku.lib.data.api.NBTData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public class ShortData extends BaseData<Short> implements NBTData<Short> {
    private ShortData(String id, short defaultValue) {
        super(id, defaultValue);
    }

    public static MutableData<Short> mutable(String id, short defaultValue) {
        return new ShortData(id, defaultValue);
    }

    public static Data<Short> immutable(String id, short defaultValue) {
        return new ShortData(id, defaultValue);
    }

    public static NBTData<Short> nbt(String id, short defaultValue) {
        return new ShortData(id, defaultValue);
    }

    @Override
    public void write(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.putShort(id(), get());
    }

    @Override
    public void read(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        set(nbt.getShort(id()));
    }
}
