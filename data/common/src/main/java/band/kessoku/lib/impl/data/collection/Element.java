package band.kessoku.lib.impl.data.collection;

import band.kessoku.lib.api.data.MutableData;
import org.jetbrains.annotations.NotNull;

public class Element<T> implements MutableData<T> {
    public final String id;
    public final int index;
    protected final ListData<T> listData;

    public Element(String id, int index, ListData<T> listData) {
        this.id = id;
        this.index = index;
        this.listData = listData;
    }

    @Override
    public void set(T newValue) {
        listData.set(index, newValue);
    }

    @Override
    public T get() {
        return listData.get(index);
    }

    @Override
    public @NotNull String id() {
        return id;
    }
}
