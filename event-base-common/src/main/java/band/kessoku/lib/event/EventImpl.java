package band.kessoku.lib.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import band.kessoku.lib.event.api.Event;
import band.kessoku.lib.event.api.EventPhase;

public class EventImpl<T> implements Event<T> {

    private Map<EventPhase, List<T>> listeners = new HashMap<>();
    private Function<List<T>, T> invokerFunc;

    public EventImpl(Function<List<T>, T> invokerFunc) {
        this.invokerFunc = invokerFunc;
        for (EventPhase phase : EventPhase.values()) {
            listeners.put(phase, List.of());
        }
    }

    @Override
    public T invoker() {
        var listenerFinal = new ArrayList<T>();
        listeners.values().forEach(listenerFinal::addAll);
        return invokerFunc.apply(listenerFinal);
    }

    @Override
    public void register(T listener, EventPhase phase) {
        listeners.get(phase).add(listener);
    }

    @Override
    public void unregister(T listener, EventPhase phase) {
        listeners.get(phase).remove(listener);
    }

    @Override
    public boolean isRegistered(T listener, EventPhase phase) {
        return listeners.get(phase).contains(listener);
    }

    @Override
    public void clearListeners(EventPhase phase) {
        listeners.get(phase).clear();
    }
}
