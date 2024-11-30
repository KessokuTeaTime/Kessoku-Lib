package band.kessoku.lib.impl;

import band.kessoku.lib.api.data.Sidinator;
import band.kessoku.lib.api.data.Storage;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class ItemSidinator implements Sidinator<Storage<ItemStack>> {
    public static Codec<ItemSidinator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.listOf().fieldOf("down").forGetter((sidinator -> sidinator.get(Direction.DOWN))),
            Codec.INT.listOf().fieldOf("up").forGetter((sidinator -> sidinator.get(Direction.UP))),
            Codec.INT.listOf().fieldOf("north").forGetter((sidinator -> sidinator.get(Direction.NORTH))),
            Codec.INT.listOf().fieldOf("south").forGetter((sidinator -> sidinator.get(Direction.SOUTH))),
            Codec.INT.listOf().fieldOf("west").forGetter((sidinator -> sidinator.get(Direction.WEST))),
            Codec.INT.listOf().fieldOf("east").forGetter((sidinator -> sidinator.get(Direction.EAST)))
    ).apply(instance, ItemSidinator::new));
    public static final PacketCodec<RegistryByteBuf, ItemSidinator> PACKET_CODEC = new PacketCodec<>() {
        @Override
        public ItemSidinator decode(RegistryByteBuf buf) {
            ItemSidinator.Builder builder = ItemSidinator.builder();
            for (Direction value : Direction.values()) {
                int size = buf.readInt();
                int[] ids = new int[size];
                for (int i = 0; i < size; i++) {
                    ids[i] = buf.readInt();
                }
                builder.add(value, ids);
            }
            return builder.build();
        }

        @Override
        public void encode(RegistryByteBuf buf, ItemSidinator value) {
            value.forEach((direction, ids) -> {
                buf.writeInt(ids.size());
                ids.forEach(buf::writeInt);
            });
        }
    };


    private final List<Integer> down;
    private final List<Integer> up;
    private final List<Integer> north;
    private final List<Integer> south;
    private final List<Integer> west;
    private final List<Integer> east;

    private ItemSidinator(
            List<Integer> down,
            List<Integer> up,
            List<Integer> north,
            List<Integer> south,
            List<Integer> west,
            List<Integer> east
            ) {
        this.down = down;
        this.up = up;
        this.north = north;
        this.south = south;
        this.west = west;
        this.east = east;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public List<Integer> get(Direction direction) {
        return switch (direction) {
            case DOWN -> down;
            case UP -> up;
            case NORTH -> north;
            case SOUTH -> south;
            case WEST -> west;
            case EAST -> east;
        };
    }

    public void forEach(BiConsumer<Direction, List<Integer>> biConsumer) {
        for (Direction value : Direction.values()) {
            biConsumer.accept(value, get(value));
        }
    }

    public static class Builder {
        private Builder() {}

        private final List<Integer> down = new ArrayList<>();
        private final List<Integer> up = new ArrayList<>();
        private final List<Integer> north = new ArrayList<>();
        private final List<Integer> south = new ArrayList<>();
        private final List<Integer> west = new ArrayList<>();
        private final List<Integer> east = new ArrayList<>();

        public Builder side(int... values) {
            return add(Direction.EAST, values).add(Direction.WEST, values).add(Direction.SOUTH, values).add(Direction.NORTH, values);
        }

        public Builder top(int... values) {
            return add(Direction.UP, values);
        }

        public Builder bottom(int... values) {
            return add(Direction.DOWN, values);
        }

        public Builder add(Direction direction, int... values) {
            get(direction).addAll(Arrays.stream(values).boxed().toList());
            return this;
        }

        private List<Integer> get(Direction direction) {
            return switch (direction) {
                case DOWN -> down;
                case UP -> up;
                case NORTH -> north;
                case SOUTH -> south;
                case WEST -> west;
                case EAST -> east;
            };
        }

        public ItemSidinator build() {
            return new ItemSidinator(
                    down.stream().distinct().toList(),
                    up.stream().distinct().toList(),
                    north.stream().distinct().toList(),
                    south.stream().distinct().toList(),
                    west.stream().distinct().toList(),
                    east.stream().distinct().toList()
            );
        }
    }
}
