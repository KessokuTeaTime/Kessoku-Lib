package band.kessoku.lib.config.api;

public interface Serializer<T> {
    void serialize(T config);
    T deserialize();
}
