package band.kessoku.lib.impl.data.base;

import band.kessoku.lib.api.data.NBTSerializable;
import band.kessoku.lib.impl.data.BaseData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public final class ItemStackData extends BaseData<ItemStack> implements NBTSerializable {
    private ItemStackData(String id, ItemStack defaultValue) {
        super(id, defaultValue);
    }

    public static ItemStackData create(String id, ItemStack defaultValue) {
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
