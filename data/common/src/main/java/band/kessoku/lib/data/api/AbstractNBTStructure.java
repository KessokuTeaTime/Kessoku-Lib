package band.kessoku.lib.data.api;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public abstract class AbstractNBTStructure extends AbstractDataStructure implements NBTSerializable {
    private final String id;

    public AbstractNBTStructure(String id) {
        this.id = id;
    }

    @Override
    public void write(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        NbtCompound root = new NbtCompound();
        for (Data<?> data : dataList) {
            if (data instanceof NBTSerializable serializable) {
                serializable.write(root, registries);
            }
        }
        for (DataStructure dataStructure : dataStructureList) {
            if (dataStructure instanceof NBTSerializable serializable) {
                serializable.write(root, registries);
            }
        }
        nbt.put(id, root);
    }

    @Override
    public void read(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        NbtCompound root = new NbtCompound();
        for (Data<?> data : dataList) {
            if (data instanceof NBTSerializable serializable) {
                serializable.read(root, registries);
            }
        }
        for (DataStructure dataStructure : dataStructureList) {
            if (dataStructure instanceof NBTSerializable serializable) {
                serializable.read(root, registries);
            }
        }
        nbt.put(id, root);
    }
}
