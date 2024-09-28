package band.kessoku.lib.impl.data.base;

import band.kessoku.lib.api.data.NBTSerializable;
import band.kessoku.lib.impl.data.BaseData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public final class ByteData extends BaseData<Byte> implements NBTSerializable {
    private ByteData(String id, byte defaultValue) {
        super(id, defaultValue);
    }

    public static ByteData create(String id, byte defaultValue) {
        return new ByteData(id, defaultValue);
    }

    @Override
    public void write(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        nbt.putByte(id(), get());
    }

    @Override
    public void read(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        set(nbt.getByte(id()));
    }
}
