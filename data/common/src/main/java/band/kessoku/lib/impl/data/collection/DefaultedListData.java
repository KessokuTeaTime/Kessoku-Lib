package band.kessoku.lib.impl.data.collection;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.ListIterator;

public final class DefaultedListData<T> extends ListData<T> {
    private final @NotNull T defaultValue;

    private DefaultedListData(String id, int size, @NotNull T defaultValue) {
        super(id);
        this.defaultValue = defaultValue;
        addAll(Collections.nCopies(size, defaultValue));
    }

    public static <T> DefaultedListData<T> create(String id, int size, @NotNull T defaultValue) {
        return new DefaultedListData<>(id, size, defaultValue);
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        ListIterator<T> it = this.listIterator(fromIndex);
        int i = 0;

        for(int n = toIndex - fromIndex; i < n; i++) {
            it.next();
            it.set(defaultValue);
        }
    }

    @Override
    public void clear() {
        for(int i = 0; i < this.size(); i++) {
            this.set(i, defaultValue);
        }
    }

    @Override
    public T remove(int index) {
        return set(index, defaultValue);
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        for (T t : this) {
            if (t != defaultValue)
                return false;
        }
        return true;
    }
}
