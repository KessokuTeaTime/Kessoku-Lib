package band.kessoku.lib.data.impl.base;

import band.kessoku.lib.data.api.NBTSerializable;
import band.kessoku.lib.data.impl.BaseData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public final class DoubleData extends BaseData<Double> implements NBTSerializable {
    private DoubleData(String id, double defaultValue) {
        super(id, defaultValue);
    }

    public static DoubleData create(String id, double defaultValue) {
        return new DoubleData(id, defaultValue);
    }

    @Override
    public void write(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        nbt.putDouble(id(), get());
    }

    @Override
    public void read(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        set(nbt.getDouble(id()));
    }
}
