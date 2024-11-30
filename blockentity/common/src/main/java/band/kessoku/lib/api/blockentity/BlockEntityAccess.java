package band.kessoku.lib.api.blockentity;

import band.kessoku.lib.api.data.DataAccess;
import band.kessoku.lib.api.data.DataStructure;
import net.minecraft.block.entity.BlockEntity;

@FunctionalInterface
public interface BlockEntityAccess<T extends BlockEntity, S extends DataStructure> extends DataAccess<T, S> {

}
