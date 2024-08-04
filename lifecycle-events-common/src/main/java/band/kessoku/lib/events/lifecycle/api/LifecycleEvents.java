package band.kessoku.lib.events.lifecycle.api;

import band.kessoku.lib.event.api.Event;

import net.minecraft.registry.DynamicRegistryManager;

public final class LifecycleEvents {

    /**
     * Called when tags are loaded or updated.
     */
    public static final Event<TagLoaded> TAG_LOADED = Event.of(tagLoadeds -> (registries, client) -> {
        for (TagLoaded tagLoaded : tagLoadeds) {
            tagLoaded.onTagsLoaded(registries, client);
        }
    });

    public interface TagLoaded {
        /**
         * @param registries Up-to-date registries from which the tags can be retrieved.
         * @param client True if the client just received a sync packet, false if the server just (re)loaded the tags.
         */
        void onTagsLoaded(DynamicRegistryManager registries, boolean client);
    }
}
