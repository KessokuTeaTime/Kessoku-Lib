package band.kessoku.lib.data.impl;

import band.kessoku.lib.data.api.Data;
import band.kessoku.lib.data.api.MutableData;
import band.kessoku.lib.data.api.NBTData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public class FloatData extends BaseData<Float> implements NBTData<Float> {
    private FloatData(String id, float defaultValue) {
        super(id, defaultValue);
    }

    public static MutableData<Float> mutable(String id, float defaultValue) {
        return new FloatData(id, defaultValue);
    }

    public static Data<Float> immutable(String id, float defaultValue) {
        return new FloatData(id, defaultValue);
    }

    public static NBTData<Float> nbt(String id, float defaultValue) {
        return new FloatData(id, defaultValue);
    }

    @Override
    public void write(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.putFloat(id(), get());
    }

    @Override
    public void read(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        set(nbt.getFloat(id()));
    }
}
