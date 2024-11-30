package band.kessoku.lib.api.data;

import net.minecraft.util.math.Direction;

import java.util.List;

public interface Sidinator<S extends DataStructure> extends DataStructure {
    List<Integer> get(Direction direction);
}
