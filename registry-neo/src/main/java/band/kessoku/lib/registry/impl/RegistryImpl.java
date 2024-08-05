package band.kessoku.lib.registry.impl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import band.kessoku.lib.event.util.NeoEventUtils;
import band.kessoku.lib.registry.api.Registry;
import com.google.auto.service.AutoService;

import net.minecraft.util.Identifier;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;

@AutoService(Registry.class)
@SuppressWarnings("unchecked")
public class RegistryImpl implements Registry {
    private static final Map<net.minecraft.registry.Registry, Set<EntryWithId>> map = new ConcurrentHashMap<>();

    // THIS NOT A PUBLIC API METHOD
    public void registerEvent(IEventBus modEventBus) {
        NeoEventUtils.registerEvent(modEventBus, RegisterEvent.class, event -> {
            map.forEach((registry, entryWithIds) ->
                    entryWithIds.forEach(entryWithId ->
                            event.register(registry.getKey(), entryWithId.id(), () -> entryWithId.entry())
                    )
            );
        });
    }

    @Override
    public <V, T extends V> T register(net.minecraft.registry.Registry<V> registry, Identifier id, T entry) {
        if (!map.containsKey(registry)) map.put(registry, new HashSet<>());
        map.get(registry).add(new EntryWithId<>(id, entry));
        return entry;
    }

    record EntryWithId<T>(Identifier id, T entry) {
    }
}
