package band.kessoku.lib.api.blockentity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Specialized {@link Data} implementation for managing lists of elements.
 * Provides list-specific operations with automatic change tracking and immutable view access.
 * <p>
 * This class extends the base Data functionality with:
 * <ul>
 *   <li>Index-based element access and modification</li>
 *   <li>Bulk operations (addAll, clear)</li>
 *   <li>Element search capabilities</li>
 *   <li>Fine-grained change hooks</li>
 * </ul>
 * All mutating operations create new list instances to trigger change callbacks.
 *
 * @param <E> The type of elements in the list
 *
 * @see Data#onValueChanged For base change notifications
 * @see #onElementChanged For per-element change notifications
 */
public abstract class ListData<E> extends Data<List<E>> {
    protected ListData(String key, List<E> defaultValue) {
        super(key, defaultValue);
    }

    public List<E> elements() {
        return Collections.unmodifiableList(get());
    }

    public int size() {
        return get().size();
    }

    public boolean isEmpty() {
        return get().isEmpty();
    }

    public void add(E element) {
        List<E> newList = new ArrayList<>(get());
        newList.add(element);
        set(newList);
    }

    public void addAll(List<E> elements) {
        if (elements == null || elements.isEmpty()) return;

        List<E> newList = new ArrayList<>(get());
        newList.addAll(elements);
        set(newList);
    }

    public E remove(int index) {
        List<E> newList = new ArrayList<>(get());
        E removed = newList.remove(index);
        set(newList);
        return removed;
    }

    public void clear() {
        if (!isEmpty()) {
            set(new ArrayList<>());
        }
    }

    public void set(int index, E newValue) {
        List<E> newList = new ArrayList<>(get());

        if (index < 0 || index >= newList.size()) {
            throw new IndexOutOfBoundsException(
                    String.format("Index: %d, Size: %d (Key: %s)", index, newList.size(), key())
            );
        }

        E oldValue = newList.get(index);
        if (!Objects.equals(oldValue, newValue)) {
            newList.set(index, newValue);
            set(newList);
            onElementChanged(index, oldValue, newValue);
        }
    }

    public E get(int index) {
        List<E> list = get();
        if (index < 0 || index >= list.size()) {
            throw new IndexOutOfBoundsException(
                    String.format("Index: %d, Size: %d (Key: %s)", index, list.size(), key())
            );
        }
        return list.get(index);
    }

    public void modifyElements(Consumer<List<E>> modifier) {
        List<E> newList = new ArrayList<>(get());
        modifier.accept(newList);
        set(newList);
    }

    public E findElement(Predicate<E> predicate) {
        return get().stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }

    protected void onElementChanged(int index, E oldValue, E newValue) {

    }
}
