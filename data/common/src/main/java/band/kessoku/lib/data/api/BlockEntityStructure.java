package band.kessoku.lib.data.api;

/**
 * Use this to realize interface injection for {@link net.minecraft.block.entity.BlockEntity BlockEntity}.
 * @see band.kessoku.lib.data.mixin.BlockEntityMixin
 */
public interface BlockEntityStructure extends DataStructure {
    /**
     * Use to integrate a {@link DataStructure DataStructure} to {@link BlockEntityStructure}.
     * @see band.kessoku.lib.data.mixin.BlockEntityMixin#integrate(DataStructure)
     */
    @Override
    default <K extends DataStructure> K integrate(K dataStructure) {
        return dataStructure;
    }

    /**
     * Use to integrate a {@link Data Data} to {@link BlockEntityStructure}.
     * @see band.kessoku.lib.data.mixin.BlockEntityMixin#integrate(Data)
     */
    @Override
    default <T, K extends Data<T>> K integrate(K data) {
        return data;
    }
}
