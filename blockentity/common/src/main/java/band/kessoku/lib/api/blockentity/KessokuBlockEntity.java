package band.kessoku.lib.api.blockentity;

import band.kessoku.lib.api.data.Storage;
import band.kessoku.lib.impl.ItemSidinator;
import net.minecraft.block.entity.BlockEntityType;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class KessokuBlockEntity {
    public static final String MOD_ID = "kessoku_blockentity";
    public static final String NAME = "Kessoku Block Entity API";
    public static final Marker MARKER = MarkerFactory.getMarker("[" + NAME +"]");

    public void init() {
        BlockEntityCoordinator.add(BlockEntityType.FURNACE, Storage::inventory);
        BlockEntityCoordinator.add(BlockEntityType.CHEST, Storage::inventory);
        BlockEntityCoordinator.add(
                BlockEntityType.FURNACE,
                (blockentity) -> ItemSidinator.builder().side(1).bottom(2, 1).top(0).build()
        );
    }
}
