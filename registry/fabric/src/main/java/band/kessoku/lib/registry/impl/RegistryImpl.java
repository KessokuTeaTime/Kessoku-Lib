package band.kessoku.lib.registry.impl;

import band.kessoku.lib.registry.api.Registry;
import com.google.auto.service.AutoService;

import net.minecraft.util.Identifier;

@AutoService(Registry.class)
public class RegistryImpl implements Registry {
    @Override
    public <V, T extends V> T register(net.minecraft.registry.Registry<V> registry, Identifier id, T entry) {
        return net.minecraft.registry.Registry.register(registry, id, entry);
    }
}
