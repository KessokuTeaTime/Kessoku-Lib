package band.kessoku.lib.data.impl;

import band.kessoku.lib.data.api.MutableData;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BaseData<T> implements MutableData<T> {
    private final String id;
    private T value;

    public BaseData(String id, T defaultValue) {
        this.id = id;
        this.value = defaultValue;
    }

    @Override
    public void set(@NotNull T newValue) {
        this.value = newValue;
    }

    @Override
    public T get() {
        return this.value;
    }

    @Override
    public @NotNull String id() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseData<?> baseData)) return false;
        return Objects.equals(value, baseData.value) && Objects.equals(id, baseData.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, id);
    }
}
