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
package band.kessoku.lib.api.networking;

import net.minecraft.server.network.ServerPlayerConfigurationTask;

public interface ServerConfigurationNetworkHandlerExtension {
    /**
     * Enqueues a {@link ServerPlayerConfigurationTask} task to be processed.
     *
     * <p>Before adding a task use {@link ServerConfigurationNetworking#canSend(ServerConfigurationNetworkHandler, Identifier)}
     * to ensure that the client can process this task.
     *
     * <p>Once the client has handled the task a packet should be sent to the server.
     * Upon receiving this packet the server should call {@link ServerConfigurationNetworkHandlerExtension#kessokulib$completeTask(ServerPlayerConfigurationTask.Key)},
     * otherwise the client cannot join the world.
     *
     * @param task the task
     */
    default void kessokulib$addTask(ServerPlayerConfigurationTask task) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    /**
     * Completes the task identified by {@code key}.
     *
     * @param key the task key
     * @throws IllegalStateException if the current task is not {@code key}
     */
    default void kessokulib$completeTask(ServerPlayerConfigurationTask.Key key) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }
}
