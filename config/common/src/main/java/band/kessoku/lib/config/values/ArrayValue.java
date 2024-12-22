package band.kessoku.lib.config.values;

import band.kessoku.lib.config.api.Codec;
import band.kessoku.lib.config.api.ConfigValue;
import com.google.common.collect.Lists;

import java.util.Collection;

public class ArrayValue extends ConfigValue<Collection<ConfigValue<?>>> {
    public ArrayValue(Codec<Collection<ConfigValue<?>>> codec) {
        super(codec, Lists.newArrayList());
    }

    public ArrayValue(Codec<Collection<ConfigValue<?>>> codec, Collection<ConfigValue<?>> object) {
        super(codec, object);
    }
}
