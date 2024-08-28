package band.kessoku.lib.config.api.serializers;

import band.kessoku.lib.config.api.Serializer;

public class JsonSerializer<T> implements Serializer<T> {
    @Override
    public void serialize(T config) {
    }

    @Override
    public T deserialize() {
        return null;
    }
}
