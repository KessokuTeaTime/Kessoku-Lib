package band.kessoku.lib.data.impl.base;

import band.kessoku.lib.data.api.Data;
import band.kessoku.lib.data.api.MutableData;
import band.kessoku.lib.data.api.NBTData;
import band.kessoku.lib.data.impl.BaseData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public final class ItemStackData extends BaseData<ItemStack> implements NBTData<ItemStack> {
    private ItemStackData(String id, ItemStack defaultValue) {
        super(id, defaultValue);
    }

    public static MutableData<ItemStack> mutable(String id, ItemStack defaultValue) {
        return new ItemStackData(id, defaultValue);
    }

    public static Data<ItemStack> immutable(String id, ItemStack defaultValue) {
        return new ItemStackData(id, defaultValue);
    }

    public static NBTData<ItemStack> nbt(String id, ItemStack defaultValue) {
        return new ItemStackData(id, defaultValue);
    }

    @Override
    public void write(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        if (!get().isEmpty()) {
            NbtCompound itemNBT = new NbtCompound();
            get().encode(registries, itemNBT);
            nbt.put(id(), itemNBT);
        }
    }

    @Override
    public void read(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        set(ItemStack.fromNbt(registries, nbt.getCompound(id())).orElse(ItemStack.EMPTY));
    }
}
