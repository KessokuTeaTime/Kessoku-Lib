package band.kessoku.lib.data.impl.structure;

import band.kessoku.lib.data.api.AbstractDataStructure;
import band.kessoku.lib.data.api.MutableData;
import net.minecraft.screen.PropertyDelegate;

import java.util.List;

@SuppressWarnings("unchecked")
public final class IntPropertyStructure extends AbstractDataStructure implements PropertyDelegate {

    private IntPropertyStructure(List<MutableData<Integer>> list) {
        list.forEach(this::integrate);
    }

    @SafeVarargs
    public static IntPropertyStructure of(MutableData<Integer>... data) {
        return new IntPropertyStructure(List.of(data));
    }

    @Override
    public int get(int index) {
        return ((MutableData<Integer>) dataList.get(index)).get();
    }

    @Override
    public void set(int index, int value) {
        ((MutableData<Integer>) dataList.get(index)).set(value);
    }

    @Override
    public int size() {
        return dataList.size();
    }
}
