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
package band.kessoku.lib.api.event;

import java.util.List;
import java.util.function.Function;

import band.kessoku.lib.impl.EventImpl;

public interface Event<T> {
    T getInvoker();

    void register(T listener, EventPhase phase);

    void clearListeners(EventPhase phase);

    default void register(T listener) {
        register(listener, EventPhase.DEFAULT);
    }

    default void clearAllListeners() {
        for (EventPhase phase : EventPhase.values()) {
            clearListeners(phase);
        }
    }

    static <T> Event<T> of(Function<List<T>, T> invokerFactory) {
        return new EventImpl<>(invokerFactory);
    }
}
