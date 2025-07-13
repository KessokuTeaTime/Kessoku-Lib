package band.kessoku.lib.impl;

import band.kessoku.lib.api.blockentity.ListData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;

/**
 * Specialized {@link ListData} implementation for storing and managing item stacks.
 * Provides inventory-like operations and optimized NBT serialization for Minecraft items.
 * <p>
 * Features:
 * <ul>
 *   <li>Slot-based access via {@link #getStack(int)} and {@link #setStack(int, ItemStack)}</li>
 *   <li>Item stack swapping with {@link #swapStacks(int, int)}</li>
 *   <li>Automatic handling of empty stacks (ItemStack.EMPTY)</li>
 *   <li>Efficient NBT storage using compound tags per stack</li>
 * </ul>
 *
 * <h3>Usage Example:</h3>
 * <pre>
 * // In KInventoryBlockEntity subclass:
 * private final ItemStorage inventory = new ItemStorage("items", DefaultedList.ofSize(9, ItemStack.EMPTY));
 *
 * public List<ItemStack> getItems() {
 *     return inventory.get();
 * }
 *
 * public ItemStack getStack(int slot) {
 *     return inventory.getStack(slot);
 * }
 * </pre>
 */
public class ItemStorage extends ListData<ItemStack> {
    public ItemStorage(String key, List<ItemStack> list) {
        super(key, list);
    }

    @Override
    public void deserialize(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        if (nbt.contains(key(), NbtElement.LIST_TYPE)) {
            NbtList listTag = nbt.getList(key(), NbtElement.COMPOUND_TYPE);
            DefaultedList<ItemStack> newList = DefaultedList.ofSize(listTag.size(), ItemStack.EMPTY);

            for (int i = 0; i < listTag.size(); i++) {
                newList.set(i, ItemStack.fromNbt(registries, listTag.getCompound(i)).orElse(ItemStack.EMPTY));
            }

            set(newList);
        }
    }

    @Override
    public void serialize(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        NbtList listTag = new NbtList();
        for (ItemStack stack : get()) {
            NbtCompound stackTag = new NbtCompound();
            stack.encode(registries, stackTag);
            listTag.add(stackTag);
        }
        nbt.put(key(), listTag);
    }

    public ItemStack getStack(int slot) {
        return get(slot);
    }

    public void setStack(int slot, ItemStack stack) {
        set(slot, stack);
    }

    public void swapStacks(int slot1, int slot2) {
        modifyElements(list -> {
            ItemStack temp = list.get(slot1);
            list.set(slot1, list.get(slot2));
            list.set(slot2, temp);
        });
    }
}
