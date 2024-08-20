package band.kessoku.lib.event.util;

import java.util.function.Consumer;

import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;

public class NeoEventUtils {
    public static <T extends Event> void registerEvent(IEventBus eventBus, Class<T> eventClass, Consumer<T> consumer) {
        eventBus.addListener(EventPriority.HIGHEST, eventClass, consumer);
    }
}
