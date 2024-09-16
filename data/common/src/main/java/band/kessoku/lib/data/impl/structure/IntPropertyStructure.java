package band.kessoku.lib.data.impl.structure;

import band.kessoku.lib.data.api.AbstractDataStructure;
import band.kessoku.lib.data.api.MutableData;
import net.minecraft.screen.PropertyDelegate;

import java.util.List;

public final class IntPropertyStructure extends AbstractDataStructure implements PropertyDelegate {
    private final List<MutableData<Integer>> list;

    private IntPropertyStructure(List<MutableData<Integer>> list) {
        this.list = list;
    }

    @SafeVarargs
    public static IntPropertyStructure of(MutableData<Integer>... data) {
        return new IntPropertyStructure(List.of(data));
    }

    @Override
    public int get(int index) {
        return list.get(index).get();
    }

    @Override
    public void set(int index, int value) {
        list.get(index).set(value);
    }

    @Override
    public int size() {
        return list.size();
    }
}
