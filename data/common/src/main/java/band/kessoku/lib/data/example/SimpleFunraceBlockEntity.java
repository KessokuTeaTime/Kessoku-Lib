package band.kessoku.lib.data.example;

import band.kessoku.lib.data.impl.collection.Access;
import band.kessoku.lib.data.impl.structure.IntPropertyStructure;
import band.kessoku.lib.data.impl.structure.ProgressStructure;
import band.kessoku.lib.data.impl.structure.SidedInventoryStructure;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public abstract class SimpleFunraceBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {
    public final SidedInventoryStructure items = integrate(new SidedInventoryStructure("Items",2));
    public final ProgressStructure progress = integrate(ProgressStructure.create("CookProgress", 200));

    public final IntPropertyStructure holder = integrate(IntPropertyStructure.of(
            progress.progressTime,
            progress.progressTimeTotal
    ));

    public final Access input = items.element("Input", 0).input(direction -> direction != Direction.DOWN);
    public final Access output = items.element("Output", 1);

    public SimpleFunraceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("container.furnace");
    }
}
