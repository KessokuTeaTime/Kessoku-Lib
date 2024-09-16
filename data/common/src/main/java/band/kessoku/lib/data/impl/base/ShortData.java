package band.kessoku.lib.data.impl.base;

import band.kessoku.lib.data.api.NBTSerializable;
import band.kessoku.lib.data.impl.BaseData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public final class ShortData extends BaseData<Short> implements NBTSerializable {
    private ShortData(String id, short defaultValue) {
        super(id, defaultValue);
    }

    public static ShortData create(String id, short defaultValue) {
        return new ShortData(id, defaultValue);
    }

    @Override
    public void write(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        nbt.putShort(id(), get());
    }

    @Override
    public void read(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        set(nbt.getShort(id()));
    }
}
