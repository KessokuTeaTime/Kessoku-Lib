package band.kessoku.lib.impl.data.base;

import band.kessoku.lib.api.data.NBTSerializable;
import band.kessoku.lib.impl.data.BaseData;
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
