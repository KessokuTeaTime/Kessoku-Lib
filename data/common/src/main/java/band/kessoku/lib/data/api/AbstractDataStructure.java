package band.kessoku.lib.data.api;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDataStructure implements DataStructure, NBTSerializable {
    private final List<Data<?>> dataList = new ArrayList<>();
    private final List<DataStructure> dataStructureList = new ArrayList<>();

    @Override
    public <T> void integrate(Data<T> data) {
        dataList.add(data);
    }

    @Override
    public void integrate(DataStructure dataStructure) {
        dataStructureList.add(dataStructure);
    }

    public List<Data<?>> getDataList() {
        return dataList;
    }

    public List<DataStructure> getDataStructureList() {
        return dataStructureList;
    }

    @Override
    public void write(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        for (Data<?> data : dataList) {
            if (data instanceof NBTSerializable serializable) {
                serializable.write(nbt, registryLookup);
            }
        }
        for (DataStructure dataStructure : dataStructureList) {
            if (dataStructure instanceof NBTSerializable serializable) {
                serializable.write(nbt, registryLookup);
            }
        }
    }

    @Override
    public void read(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        for (Data<?> data : dataList) {
            if (data instanceof NBTSerializable serializable) {
                serializable.read(nbt, registryLookup);
            }
        }
        for (DataStructure dataStructure : dataStructureList) {
            if (dataStructure instanceof NBTSerializable serializable) {
                serializable.read(nbt, registryLookup);
            }
        }
    }
}
