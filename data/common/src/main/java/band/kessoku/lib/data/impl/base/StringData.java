package band.kessoku.lib.data.impl.base;

import band.kessoku.lib.data.api.Data;
import band.kessoku.lib.data.api.MutableData;
import band.kessoku.lib.data.api.NBTData;
import band.kessoku.lib.data.impl.BaseData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public final class StringData extends BaseData<String> implements NBTData<String> {
    private StringData(String id, String defaultValue) {
        super(id, defaultValue);
    }

    public static MutableData<String> mutable(String id, String defaultValue) {
        return new StringData(id, defaultValue);
    }

    public static Data<String> immutable(String id, String defaultValue) {
        return new StringData(id, defaultValue);
    }

    public static NBTData<String> nbt(String id, String defaultValue) {
        return new StringData(id, defaultValue);
    }

    @Override
    public void write(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        nbt.putString(id(), get());
    }

    @Override
    public void read(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        set(nbt.getString(id()));
    }
}
