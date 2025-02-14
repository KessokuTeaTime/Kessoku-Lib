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
package band.kessoku.lib.api.networking.server;

import band.kessoku.lib.event.api.Event;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerConfigurationNetworkHandler;

/**
 * Offers access to events related to the connection to a client on a logical server while a client is configuring.
 */
public final class ServerConfigurationConnectionEvent {
    /**
     * Event fired before any vanilla configuration has taken place.
     *
     * <p>This event is executed on {@linkplain io.netty.channel.EventLoop netty's event loops}.
     *
     * <p>Task queued during this event will complete before vanilla configuration starts.
     */
    public static final Event<Configure> BEFORE_CONFIGURE = Event.of(beforeConfigures -> (handler, server) -> {
        for (Configure callback : beforeConfigures) {
            callback.onSendConfiguration(handler, server);
        }
    });

    /**
     * Event fired during vanilla configuration.
     *
     * <p>This event is executed on {@linkplain io.netty.channel.EventLoop netty's event loops}.
     *
     * <p>An example usage of this:
     * <pre>{@code
     * ServerConfigurationConnectionEvents.CONFIGURE.register((handler, server) -> {
     * 	if (ServerConfigurationNetworking.canSend(handler, ConfigurationPacket.PACKET_TYPE)) {
     *  handler.addTask(new TestConfigurationTask("Example data"));
     * 	} else {
     * 	  // You can opt to disconnect the client if it cannot handle the configuration task
     * 	  handler.disconnect(Text.literal("Network test configuration not supported by client"));
     * 	  }
     * });
     * }</pre>
     */
    public static final Event<Configure> CONFIGURE = Event.of(configures -> (handler, server) -> {
        for (Configure callback : configures) {
            callback.onSendConfiguration(handler, server);
        }
    });

    /**
     * An event for the disconnection of the server configuration network handler.
     *
     * <p>No packets should be sent when this event is invoked.
     */
    public static final Event<Disconnect> DISCONNECT = Event.of(disconnects -> (handler, server) -> {
        for (Disconnect callback : disconnects) {
            callback.onConfigureDisconnect(handler, server);
        }
    });

    @FunctionalInterface
    public interface Configure {
        void onSendConfiguration(ServerConfigurationNetworkHandler handler, MinecraftServer server);
    }

    @FunctionalInterface
    public interface Disconnect {
        void onConfigureDisconnect(ServerConfigurationNetworkHandler handler, MinecraftServer server);
    }
}
