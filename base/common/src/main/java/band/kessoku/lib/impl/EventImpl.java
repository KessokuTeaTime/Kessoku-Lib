/*
 * Copyright (c) 2024, 2025 KessokuTeaTime
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
package band.kessoku.lib.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import band.kessoku.lib.api.event.Event;
import band.kessoku.lib.api.event.EventPhase;

public final class EventImpl<T> implements Event<T> {
    private final List<List<T>> listeners = new ArrayList<>(5);
    private final Function<List<T>, T> invokerFactory;

    public EventImpl(Function<List<T>, T> invokerFactory) {
        this.invokerFactory = invokerFactory;
    }

    @Override
    public T getInvoker() {
        var sortedListeners = new ArrayList<T>();
        listeners.forEach(sortedListeners::addAll);
        return invokerFactory.apply(sortedListeners);
    }

    @Override
    public void register(T listener, EventPhase phase) {
        listeners.add(phase.ordinal(), new ArrayList<>());
        listeners.get(phase.ordinal()).add(listener);
    }

    @Override
    public void clearListeners(EventPhase phase) {
        listeners.get(phase.ordinal()).clear();
    }
}
