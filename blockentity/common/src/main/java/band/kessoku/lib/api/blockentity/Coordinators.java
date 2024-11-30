package band.kessoku.lib.api.blockentity;

import band.kessoku.lib.api.data.Storage;
import band.kessoku.lib.impl.ItemSidinator;
import net.minecraft.item.ItemStack;

public class Coordinators {
    public static final Coordinator<Storage<ItemStack>> ITEM = new Coordinator<>();
    public static final Coordinator<ItemSidinator> ITEM_SIDE = new Coordinator<>();
}
