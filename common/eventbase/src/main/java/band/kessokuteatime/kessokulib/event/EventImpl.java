package band.kessokuteatime.kessokulib.event;

import band.kessokuteatime.kessokulib.event.api.Event;
import band.kessokuteatime.kessokulib.event.api.EventPhase;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Map;

public class EventImpl<T> implements Event<T> {

    private Map<EventPhase, List<T>> listeners;

    public EventImpl() {
        for (EventPhase phase : EventPhase.values()) {
            listeners.put(phase, List.of());
        }
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

    public ImmutableList<T> get(EventPhase phase) {
        return ImmutableList.copyOf(listeners.get(phase));
    }
}
