package band.kessoku.lib.config.api;

import java.util.function.Function;

public abstract class ConfigCodec<A, B extends ConfigValue<?>> {
    private final Function<A, B> encode;
    private final Function<B, A> decode;

    public ConfigCodec(Function<A, B> encode, Function<B, A> decode) {
        this.encode = encode;
        this.decode = decode;
    }

    public B encode(A value) {
        return encode.apply(value);
    }

    public A decode(B value) {
        return decode.apply(value);
    }
}
