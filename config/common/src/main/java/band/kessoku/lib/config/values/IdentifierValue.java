package band.kessoku.lib.config.values;

import band.kessoku.lib.config.api.Codec;
import band.kessoku.lib.config.api.ConfigValue;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class IdentifierValue extends ConfigValue<Identifier> {
    public static final Codec<Identifier> CODEC = new Codec<>() {
        @Override
        public Identifier encode(String valueStr) {
            return Identifier.of(valueStr);
        }

        @Override
        public String decode(Identifier value) {
            return value.toString();
        }
    };

    public IdentifierValue(String name) {
        super(CODEC, Identifier.of(name));
    }

    public IdentifierValue(Identifier object) {
        super(CODEC, object);
    }

    public IdentifierValue(Codec<Identifier> codec, String name) {
        super(codec, Identifier.of(name));
    }

    public IdentifierValue(Codec<Identifier> codec, Identifier object) {
        super(codec, object);
    }

    public Item asItem() {
        return Registries.ITEM.get(this.getValue());
    }

    public Block asBlock() {
        return Registries.BLOCK.get(this.getValue());
    }
}
