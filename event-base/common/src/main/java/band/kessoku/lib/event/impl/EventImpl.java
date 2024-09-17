/*
 * Copyright (c) 2024 KessokuTeaTime
 *
 * Licensed under the GNU Lesser General Pubic License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package band.kessoku.lib.event.impl;

import band.kessoku.lib.event.api.Event;
import band.kessoku.lib.event.api.EventPhase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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
