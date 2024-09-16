package band.kessoku.lib.data.api;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDataStructure implements DataStructure {
    protected final List<Data<?>> dataList = new ArrayList<>();
    protected final List<DataStructure> dataStructureList = new ArrayList<>();

    @Override
    public <T, K extends Data<T>> K integrate(K data) {
        dataList.add(data);
        return data;
    }

    @Override
    public DataStructure integrate(DataStructure dataStructure) {
        dataStructureList.add(dataStructure);
        return dataStructure;
    }
}
