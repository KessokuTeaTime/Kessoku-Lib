package band.kessoku.lib.resources.api.lifecycle;

import band.kessoku.lib.event.api.Event;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceType;

import java.util.List;

public interface PackRegistryEvent {

    Event<BeforeUser> BEFORE_USER = Event.of((beforeUsers -> ((type, packs) -> {
        for (BeforeUser beforeUser : beforeUsers) {
            beforeUser.registry(type, packs);
        }
    })));

    Event<BeforeVanilla> BEFORE_VANILLA = Event.of((beforeVanillas -> ((type, packs) -> {
        for (BeforeVanilla beforeVanilla : beforeVanillas) {
            beforeVanilla.registry(type, packs);
        }
    })));

    Event<AfterVanilla> AFTER_VANILLA = Event.of((afterVanillas -> ((type, packs) -> {
        for (AfterVanilla afterVanilla : afterVanillas) {
            afterVanilla.registry(type, packs);
        }
    })));

    interface BeforeUser extends PackRegistryEvent { }
    interface BeforeVanilla extends PackRegistryEvent { }
    interface AfterVanilla extends PackRegistryEvent { }

    void registry(ResourceType type, List<ResourcePack> packs);
}
