package band.kessoku.lib.config.api;

import java.nio.file.Path;

public interface ConfigHolder<T> {
    Class<T> getConfigClass();
    T getConfig();
    void save();
    boolean load();
    void reset();
    void set(T config);
    String getConfigName();
    Path getConfigPath();
}
