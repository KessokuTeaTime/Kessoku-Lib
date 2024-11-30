package band.kessoku.lib.api.data;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public interface Storage<T> extends DataStructure {
    T get(int id);

    int size();

    static Storage<ItemStack> inventory(Inventory inventory) {
        return new Storage<>() {
            @Override
            public ItemStack get(int id) {
                return inventory.getStack(id);
            }

            @Override
            public int size() {
                return inventory.size();
            }
        };
    }
}
