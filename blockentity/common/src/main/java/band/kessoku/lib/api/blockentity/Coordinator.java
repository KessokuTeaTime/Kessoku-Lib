package band.kessoku.lib.api.blockentity;

import band.kessoku.lib.api.data.DataStructure;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.function.TriFunction;

import java.util.HashMap;
import java.util.Map;

public class Coordinator<S extends DataStructure> {
    private final Map<BlockEntityType<?>, TriFunction<BlockEntityType<?>, World, BlockPos, S>> map = new HashMap<>();

    public <T extends BlockEntity> void add(BlockEntityType<T> type, BlockEntityAccess<T, S> access) {
        map.put(type, ((t, world, blockPos) -> {
            if (t == type) {
                return access.to(type.get(world, blockPos));
            }
            return null;
        }));
    }

    public <T extends BlockEntity> S get(BlockEntityType<T> type, World world, BlockPos pos) {
        return map.get(type).apply(type, world, pos);
    }
}
