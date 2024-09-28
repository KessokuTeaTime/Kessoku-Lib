package band.kessoku.lib.impl.data.structure;

import band.kessoku.lib.api.data.AbstractDataStructure;
import band.kessoku.lib.api.data.NBTSerializable;
import band.kessoku.lib.impl.data.collection.DefaultedListData;
import band.kessoku.lib.impl.data.collection.Element;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;

import java.util.List;

public class InventoryStructure extends AbstractDataStructure implements NBTSerializable, Inventory {
    public final DefaultedListData<ItemStack> inventory;

    public InventoryStructure(String id, int size) {
        inventory = integrate(DefaultedListData.create(id, size, ItemStack.EMPTY));
    }

    public Element<ItemStack> element(String id, int index) {
        return new Element<>(id, index, inventory);
    }

    public ContainerComponent toComponent() {
        return ContainerComponent.fromStacks(inventory);
    }

    public void copy(List<ItemStack> copySource) {
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack itemStack = i < copySource.size() ? copySource.get(i) : ItemStack.EMPTY;
            inventory.set(i, itemStack.copy());
        }
    }

    public void copy(ContainerComponent copySource) {
        copy(copySource.stream().toList());
    }

    @Override
    public void write(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        NbtList nbtList = new NbtList();

        for(int i = 0; i < inventory.size(); i++) {
            ItemStack itemStack = inventory.get(i);
            if (!itemStack.isEmpty()) {
                NbtCompound nbtCompound = new NbtCompound();
                nbtCompound.putByte("Slot", (byte)i);
                nbtList.add(itemStack.encode(registries, nbtCompound));
            }
        }

        nbt.put(inventory.id(), nbtList);
    }

    @Override
    public void read(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        inventory.clear();

        NbtList nbtList = nbt.getList(inventory.id(), 10);

        for(int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            int j = nbtCompound.getByte("Slot") & 255;
            if (j < inventory.size()) {
                inventory.set(j, ItemStack.fromNbt(registries, nbtCompound).orElse(ItemStack.EMPTY));
            }
        }
    }

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack itemStack = Inventories.splitStack(this.inventory, slot, amount);
        if (!itemStack.isEmpty()) {
            this.markDirty();
        }

        return itemStack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.inventory.set(slot, stack);
        stack.capCount(this.getMaxCount(stack));
        this.markDirty();
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }
}
