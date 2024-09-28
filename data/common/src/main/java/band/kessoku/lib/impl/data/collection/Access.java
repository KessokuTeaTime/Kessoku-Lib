package band.kessoku.lib.impl.data.collection;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

import java.util.function.Predicate;

public class Access extends Element<ItemStack> {
    public Predicate<Direction> input = direction -> true;
    public Predicate<Direction> output = direction -> true;
    public Predicate<ItemStack> itemstack = itemStack -> true;

    private Access(String id, int index, ListData<ItemStack> listData) {
        super(id, index, listData);
    }

    public static Access create(String id, int index, ListData<ItemStack> listData) {
        return new Access(id, index, listData);
    }

    public Access input(Predicate<Direction> condition) {
        input = input.and(condition);
        return this;
    }

    public Access output(Predicate<Direction> condition) {
        output = output.and(condition);
        return this;
    }

    public Access condition(Predicate<ItemStack> condition) {
        itemstack = itemstack.and(condition);
        return this;
    }
}
