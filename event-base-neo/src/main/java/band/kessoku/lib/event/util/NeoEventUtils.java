package band.kessoku.lib.event.util;

import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;

import java.util.function.Consumer;

public class NeoEventUtils {
    public static <T extends Event> void registerEvent(IEventBus eventBus, Class<T> eventClass, Consumer<T> consumer) {
        eventBus.addListener(EventPriority.HIGHEST, eventClass, consumer);
    }
}
