package band.kessoku.lib.data.impl.base;

import band.kessoku.lib.data.api.Data;
import band.kessoku.lib.data.api.MutableData;
import band.kessoku.lib.data.api.NBTData;
import band.kessoku.lib.data.impl.BaseData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public final class ByteData extends BaseData<Byte> implements NBTData<Byte> {
    private ByteData(String id, byte defaultValue) {
        super(id, defaultValue);
    }

    public static MutableData<Byte> mutable(String id, byte defaultValue) {
        return new ByteData(id, defaultValue);
    }

    public static Data<Byte> immutable(String id, byte defaultValue) {
        return new ByteData(id, defaultValue);
    }

    public static NBTData<Byte> nbt(String id, byte defaultValue) {
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
