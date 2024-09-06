package band.kessoku.lib.registry.api;

import band.kessoku.lib.event.api.Event;

public class RegistryEvent {

    public static Event<Registry> REGISTRY = Event.of(registries -> registry -> {
        registries.forEach((registry1 -> registry1.registry(registry)));
    });

    public interface Registry {

        void registry(band.kessoku.lib.registry.api.Registry registry);

    }

}
