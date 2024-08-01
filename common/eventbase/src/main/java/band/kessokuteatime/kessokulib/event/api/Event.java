package band.kessokuteatime.kessokulib.event.api;

import band.kessokuteatime.kessokulib.event.EventImpl;

public interface Event<T> {

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

    static <T> Event<T> of() {
        return new EventImpl<>();
    }

}
