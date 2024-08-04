package band.kessoku.lib.registry.impl;

import band.kessoku.lib.registry.KessokuRegistry;
import band.kessoku.lib.registry.api.Registry;
import com.google.auto.service.AutoService;
import net.minecraft.util.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@AutoService(Registry.class)
@EventBusSubscriber(modid = KessokuRegistry.MOD_ID)
@SuppressWarnings("unchecked")
public class RegistryImpl implements Registry {
    private static final Map<net.minecraft.registry.Registry, Set<EntryWithId>> map = new ConcurrentHashMap<>();

    @SubscribeEvent
    public void register(RegisterEvent event) {
        map.forEach((registry, entryWithIds) ->
                entryWithIds.forEach(entryWithId ->
                        event.register(registry.getKey(), entryWithId.id(), () -> entryWithId.entry())
                )
        );
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
