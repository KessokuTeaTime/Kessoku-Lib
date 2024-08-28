package band.kessoku.lib.config.impl;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import band.kessoku.lib.config.api.ConfigHolder;

public final class ConfigHolderImpl<T> implements ConfigHolder<T> {
    private final Class<T> configClass;
    private T config;

    public ConfigHolderImpl(Class<T> configClass) {
        this.configClass = configClass;
        this.reset();
    }

    @Override
    public Class<T> getConfigClass() {
        return this.configClass;
    }

    @Override
    public T getConfig() {
        return config;
    }

    @Override
    public void save() {
        Path configPath = this.getConfigPath();
        if (Files.notExists(configPath)) {
            try {
                Files.createFile(configPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try (FileWriter writer = new FileWriter(configPath.toFile(), StandardCharsets.UTF_8)) {
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean load() {
        return false;
    }

    @Override
    public void reset() {
    }

    @Override
    public void set(T config) {
        this.config = config;
    }

    @Override
    public String getConfigName() {
        return "";
    }

    @Override
    public Path getConfigPath() {
        return Path.of("");
    }
}
