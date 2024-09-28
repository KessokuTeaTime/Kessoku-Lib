package band.kessoku.lib.impl.data.structure;

import band.kessoku.lib.impl.data.collection.Access;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class SidedInventoryStructure extends InventoryStructure implements SidedInventory {
    private final List<Access> list = new ArrayList<>();

    public SidedInventoryStructure(String id, int size) {
        super(id, size);
    }

    @Override
    public Access element(String id, int index) {
        Access access = Access.create(id, index, this.inventory);
        list.add(access);
        return access;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return list.stream()
                .filter(access -> access.input.test(side) || access.output.test(side))
                .mapToInt(access -> access.index)
                .toArray();
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return list.stream()
                .filter(access -> access.index == slot)
                .findFirst()
                .map(access -> access.input.test(dir) && access.itemstack.test(stack))
                .orElse(false);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return list.stream()
                .filter(access -> access.index == slot)
                .findFirst()
                .map(access -> access.output.test(dir) && access.itemstack.test(stack))
                .orElse(false);
    }

}
