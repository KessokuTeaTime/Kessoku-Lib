package band.kessoku.lib.data.impl.base;

import band.kessoku.lib.data.api.NBTSerializable;
import band.kessoku.lib.data.impl.BaseData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

public final class IdentifierData extends BaseData<Identifier> implements NBTSerializable {
    private IdentifierData(String id, Identifier defaultValue) {
        super(id, defaultValue);
    }

    public static IdentifierData create(String id, Identifier defaultValue) {
        return new IdentifierData(id, defaultValue);
    }

    @Override
    public void write(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        nbt.putString(id(), get().toString());
    }

    @Override
    public void read(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        set(Identifier.of(nbt.getString(id())));
    }
}
