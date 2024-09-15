package band.kessoku.lib.data.impl;

import band.kessoku.lib.data.api.Data;
import band.kessoku.lib.data.api.MutableData;
import band.kessoku.lib.data.api.NBTData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public class DoubleData extends BaseData<Double> implements NBTData<Double> {
    private DoubleData(String id, double defaultValue) {
        super(id, defaultValue);
    }

    public static MutableData<Double> mutable(String id, double defaultValue) {
        return new DoubleData(id, defaultValue);
    }

    public static Data<Double> immutable(String id, double defaultValue) {
        return new DoubleData(id, defaultValue);
    }

    public static NBTData<Double> nbt(String id, double defaultValue) {
        return new DoubleData(id, defaultValue);
    }

    @Override
    public void write(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.putDouble(id(), get());
    }

    @Override
    public void read(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        set(nbt.getDouble(id()));
    }
}
