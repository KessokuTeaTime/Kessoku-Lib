package band.kessoku.lib.config.api;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

public class SerializableConfigValue<A, B extends ConfigValue<?>> {
    public ConfigCodec<A, B> codec;
    protected A value;
    final B defaultValue;
    List<String> comments = new ArrayList<>();
    @Nullable
    String name;

    public SerializableConfigValue(ConfigCodec<A, B> codec, A value, A defaultValue) {
        this(codec, value, defaultValue, null);
    }

    public SerializableConfigValue(ConfigCodec<A, B> codec, A value, A defaultValue, @Nullable String name) {
        this(codec, value, codec.encode(defaultValue), name);
    }

    public SerializableConfigValue(ConfigCodec<A, B> codec, A value, B defaultValue) {
        this(codec, value, defaultValue, null);
    }

    public SerializableConfigValue(ConfigCodec<A, B> codec, A value, B defaultValue, @Nullable String name) {
        this.codec = codec;
        this.value = value;
        this.defaultValue = defaultValue;
        this.name = name;
    }

    public SerializableConfigValue<A, B> addComment(String comment) {
        this.comments.add(comment);
        return this;
    }

    public void reset() {
        this.value = this.codec.decode(this.defaultValue);
    }

    public A get() {
        return this.value;
    }

    public void set(A value) {
        this.value = value;
    }

    public void set(B value) {
        this.value = this.codec.decode(value);
    }
}
