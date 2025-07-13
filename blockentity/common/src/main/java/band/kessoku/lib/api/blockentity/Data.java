package band.kessoku.lib.api.blockentity;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Represents a typed data value that can be serialized to NBT and provides change tracking.
 * This abstract class manages a single value associated with a unique key, handling default values,
 * value changes, and NBT serialization. Subclasses must implement NBT serialization logic.
 * <p>
 * When the stored value changes, {@link #onValueChanged} is invoked automatically.
 * This class provides utility methods for safe modification and type-safe retrieval.
 *
 * @param <T> The type of data managed by this instance
 *
 * @see #modify(Consumer) For transactional modifications
 * @see #update(Function) For functional updates
 * @see #reset() To revert to default value
 */
public abstract class Data<T> {
    private final String key;
    private T value;
    private final Supplier<T> defaultValueSupplier;

    /**
     * Constructs a Data instance with a fixed default value.
     *
     * @param key          The unique NBT key for serialization (must not be null)
     * @param defaultValue The default value used during initialization and reset
     * @throws NullPointerException if key is null
     */
    protected Data(String key, T defaultValue) {
        this.key = Objects.requireNonNull(key, "Key cannot be null");
        this.value = defaultValue;
        this.defaultValueSupplier = () -> defaultValue;
    }

    /**
     * Constructs a Data instance with a dynamic default value supplier.
     *
     * @param key                   The unique NBT key for serialization (must not be null)
     * @param defaultValueSupplier  Supplier for the default value (must not be null)
     * @throws NullPointerException if key or defaultValueSupplier is null
     */
    protected Data(String key, Supplier<T> defaultValueSupplier) {
        this.key = Objects.requireNonNull(key, "Key cannot be null");
        this.defaultValueSupplier = Objects.requireNonNull(defaultValueSupplier);
        this.value = defaultValueSupplier.get();
    }

    /**
     * Gets the current value. May return {@code null} if not initialized.
     *
     * @return The current value (could be null)
     */
    public T get() {
        return value;
    }

    /**
     * Gets the current value if non-null, otherwise returns the default value.
     *
     * @return Current value if present, otherwise default value
     */
    public T getOrDefault() {
        return value != null ? value : defaultValueSupplier.get();
    }

    /**
     * Gets the value cast to a specified type.
     *
     * @param <R>  The target type
     * @param type The target class to cast to
     * @return Value cast to the specified type
     * @throws ClassCastException if the value is not an instance of the target type
     */
    public <R> R getAs(Class<R> type) {
        if (type.isInstance(value)) {
            return type.cast(value);
        }
        throw new ClassCastException("Value is not of type " + type.getName());
    }

    /**
     * Sets a new value and triggers change callbacks if different from current value.
     *
     * @param newValue The new value to set
     * @return The previous value
     */
    public T set(T newValue) {
        T oldValue = this.value;
        if (!Objects.equals(oldValue, newValue)) {
            this.value = newValue;
            onValueChanged(oldValue, newValue);
        }
        return oldValue;
    }

    /**
     * Resets the value to the default and triggers change callbacks if different.
     *
     * @return The previous value
     */
    public T reset() {
        return set(defaultValueSupplier.get());
    }

    /**
     * Hook method invoked when the value changes. Does nothing by default.
     * Subclasses may override to implement custom change logic.
     *
     * @param oldValue The value before change
     * @param newValue The value after change
     */
    protected void onValueChanged(T oldValue, T newValue) {
        // Optional override point for subclasses
    }

    /**
     * Modifies the value in-place using a consumer. Triggers change callbacks if modified.
     * <p>
     * Example: {@code data.modify(list -> list.add(item))}
     *
     * @param modifier The modification operation (must not be null)
     * @throws NullPointerException if modifier is null
     */
    public void modify(Consumer<T> modifier) {
        Objects.requireNonNull(modifier, "Modifier cannot be null");
        T oldValue = this.value;
        modifier.accept(this.value);
        if (!Objects.equals(oldValue, this.value)) {
            onValueChanged(oldValue, this.value);
        }
    }

    /**
     * Updates the value using a function. Triggers change callbacks if result differs.
     * <p>
     * Example: {@code data.update(count -> count + 1)}
     *
     * @param updater The update function (must not be null)
     * @throws NullPointerException if updater is null
     */
    public void update(Function<T, T> updater) {
        Objects.requireNonNull(updater, "Updater cannot be null");
        T oldValue = this.value;
        T newValue = updater.apply(this.value);
        if (!Objects.equals(oldValue, newValue)) {
            this.value = newValue;
            onValueChanged(oldValue, newValue);
        }
    }

    /**
     * Checks if the current value is {@code null}.
     *
     * @return {@code true} if the value is null, {@code false} otherwise
     */
    public boolean isNull() {
        return value == null;
    }

    /**
     * Gets the NBT key associated with this data.
     *
     * @return The serialization key (guaranteed non-null)
     */
    public String key() {
        return key;
    }

    /**
     * Loads the value from NBT data. Must be implemented by subclasses.
     *
     * @param nbt       The NBT compound to read from
     * @param registries Registry access for resolving registry entries
     */
    public abstract void deserialize(NbtCompound nbt, RegistryWrapper.WrapperLookup registries);

    /**
     * Saves the value to NBT data. Must be implemented by subclasses.
     *
     * @param nbt       The NBT compound to write to
     * @param registries Registry access for resolving registry entries
     */
    public abstract void serialize(NbtCompound nbt, RegistryWrapper.WrapperLookup registries);
}