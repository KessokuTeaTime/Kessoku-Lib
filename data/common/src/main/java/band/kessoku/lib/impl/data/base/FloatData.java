package band.kessoku.lib.impl.data.base;

import band.kessoku.lib.api.data.NBTSerializable;
import band.kessoku.lib.impl.data.BaseData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public final class FloatData extends BaseData<Float> implements NBTSerializable {
    private FloatData(String id, float defaultValue) {
        super(id, defaultValue);
    }

    public static FloatData create(String id, float defaultValue) {
        return new FloatData(id, defaultValue);
    }

    @Override
    public void write(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        nbt.putFloat(id(), get());
    }

    @Override
    public void read(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        set(nbt.getFloat(id()));
    }
}
