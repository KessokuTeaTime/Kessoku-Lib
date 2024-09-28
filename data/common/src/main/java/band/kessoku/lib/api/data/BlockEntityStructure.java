package band.kessoku.lib.api.data;

import band.kessoku.lib.mixins.data.BlockEntityMixin;

/**
 * Use this to realize interface injection for {@link net.minecraft.block.entity.BlockEntity BlockEntity}.
 * @see BlockEntityMixin
 */
public interface BlockEntityStructure extends DataStructure {
    /**
     * Use to integrate a {@link DataStructure DataStructure} to {@link BlockEntityStructure}.
     * @see BlockEntityMixin#integrate(DataStructure)
     */
    @Override
    default <K extends DataStructure> K integrate(K dataStructure) {
        return dataStructure;
    }

    /**
     * Use to integrate a {@link Data Data} to {@link BlockEntityStructure}.
     * @see BlockEntityMixin#integrate(Data)
     */
    @Override
    default <T, K extends Data<T>> K integrate(K data) {
        return data;
    }
}
