package band.kessoku.lib.data.api;

import net.minecraft.component.ComponentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.Nullable;

public interface ComponentData<T> extends MutableData<T> {
    void read(ComponentsAccess components);
    void read(NbtCompound nbt, RegistryWrapper.WrapperLookup registries);

    interface ComponentsAccess {
        @Nullable
        <T> T get(ComponentType<T> type);

        <T> T getOrDefault(ComponentType<? extends T> type, T fallback);
    }
}
