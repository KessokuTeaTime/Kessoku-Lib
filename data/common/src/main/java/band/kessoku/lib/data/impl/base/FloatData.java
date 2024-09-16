package band.kessoku.lib.data.impl.base;

import band.kessoku.lib.data.api.Data;
import band.kessoku.lib.data.api.MutableData;
import band.kessoku.lib.data.api.NBTData;
import band.kessoku.lib.data.impl.BaseData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public final class FloatData extends BaseData<Float> implements NBTData<Float> {
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
    public void write(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        nbt.putFloat(id(), get());
    }

    @Override
    public void read(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        set(nbt.getFloat(id()));
    }
}
