package band.kessoku.lib.api.blockentity;

import band.kessoku.lib.api.data.DataStructure;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

public class BlockEntityCoordinator {
    public static <T extends BlockEntity, S extends DataStructure> void add(BlockEntityType<T> type, BlockEntityAccess<T, S> access) {

    }
}
