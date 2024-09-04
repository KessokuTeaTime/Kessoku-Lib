package kessoku.testmod.event;

import band.kessoku.lib.entrypoint.api.KessokuModInitializer;
import band.kessoku.lib.event.api.Event;

public class KessokuTestEvent implements KessokuModInitializer {

    public static final Event<TestEvent> EVENT = Event.of(testEvents -> (name) -> {
        for (TestEvent event : testEvents) {
            event.test(name);
        }
    });

    @Override
    public void onInitialize() {
        EVENT.invoker().test("Hello, world!");
    }

    public interface TestEvent {
        void test(String name);
    }
}
