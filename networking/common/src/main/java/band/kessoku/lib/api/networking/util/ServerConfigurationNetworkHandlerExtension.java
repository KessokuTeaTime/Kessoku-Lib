package band.kessoku.lib.api.networking.util;

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
