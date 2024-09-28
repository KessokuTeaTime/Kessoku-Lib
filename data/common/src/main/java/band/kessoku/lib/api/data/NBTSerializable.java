package band.kessoku.lib.api.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

/**
 * An interface used to normalize serialization of {@link NbtCompound NBT}.
 */
public interface NBTSerializable {
    void write(NbtCompound nbt, RegistryWrapper.WrapperLookup registries);
    void read(NbtCompound nbt, RegistryWrapper.WrapperLookup registries);
}
