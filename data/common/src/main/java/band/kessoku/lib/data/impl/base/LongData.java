package band.kessoku.lib.data.impl.base;

import band.kessoku.lib.data.api.NBTSerializable;
import band.kessoku.lib.data.impl.BaseData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public final class LongData extends BaseData<Long> implements NBTSerializable {
    private LongData(String id, long defaultValue) {
        super(id, defaultValue);
    }

    public static LongData create(String id, long defaultValue) {
        return new LongData(id, defaultValue);
    }

    @Override
    public void write(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        nbt.putLong(id(), get());
    }

    @Override
    public void read(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        set(nbt.getLong(id()));
    }
}
