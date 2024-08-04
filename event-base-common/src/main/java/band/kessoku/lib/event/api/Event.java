package band.kessoku.lib.event.api;

import java.util.List;
import java.util.function.Function;

import band.kessoku.lib.event.EventImpl;

public interface Event<T> {

    T invoker();

    void register(T listener, EventPhase phase);

    void unregister(T listener, EventPhase phase);

    boolean isRegistered(T listener, EventPhase phase);

    void clearListeners(EventPhase phase);

    default void register(T listener) {
        register(listener, EventPhase.DEFAULT);
    };

    default void unregister(T listener) {
        unregister(listener, EventPhase.DEFAULT);
    };

    default boolean isRegistered(T listener) {
        return isRegistered(listener, EventPhase.DEFAULT);
    };

    default void clearAllListeners() {
        for (EventPhase phase : EventPhase.values()) {
            clearListeners(phase);
        }
    };

    static <T> Event<T> of(Function<List<T>, T> invokerFunc) {
        return new EventImpl<>(invokerFunc);
    }

}
