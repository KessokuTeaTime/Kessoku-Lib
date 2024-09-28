package band.kessoku.lib.impl.data.base;

import band.kessoku.lib.api.data.NBTSerializable;
import band.kessoku.lib.impl.data.BaseData;
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
