package band.kessoku.lib.data.impl;

import band.kessoku.lib.data.api.Data;
import band.kessoku.lib.data.api.MutableData;
import band.kessoku.lib.data.api.NBTData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

import java.util.UUID;

public class UUIDData extends BaseData<UUID> implements NBTData<UUID> {
    private UUIDData(String id, UUID defaultValue) {
        super(id, defaultValue);
    }

    public static MutableData<UUID> mutable(String id, UUID defaultValue) {
        return new UUIDData(id, defaultValue);
    }

    public static Data<UUID> immutable(String id, UUID defaultValue) {
        return new UUIDData(id, defaultValue);
    }

    public static NBTData<UUID> nbt(String id, UUID defaultValue) {
        return new UUIDData(id, defaultValue);
    }

    @Override
    public void write(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.putUuid(id(), get());
    }

    @Override
    public void read(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        set(nbt.getUuid(id()));
    }
}
