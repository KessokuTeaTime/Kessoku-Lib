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
package band.kessoku.lib.api.networking.client;

import band.kessoku.lib.event.api.Event;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientConfigurationNetworkHandler;
import net.minecraft.network.packet.CustomPayload;

/**
 * Offers access to events related to the configuration connection to a server on a logical client.
 */
public final class ClientConfigurationConnectionEvent {
    /**
     * Event indicating a connection entering the CONFIGURATION state, ready for registering channel handlers.
     *
     * <p>No packets should be sent when this event is invoked.
     *
     * @see ClientConfigurationNetworking#registerReceiver(CustomPayload.Id, ClientConfigurationNetworking.ConfigurationPayloadHandler)
     */
    public static final Event<Init> INIT = Event.of(inits -> (handler, client) -> {
        for (ClientConfigurationConnectionEvent.Init callback : inits) {
            callback.onConfigurationInit(handler, client);
        }
    });

    /**
     * An event called after the connection has been initialized and is ready to start sending and receiving configuration packets.
     *
     * <p>Packets may be sent during this event.
     */
    public static final Event<Start> START = Event.of(starts -> (handler, client) -> {
        for (ClientConfigurationConnectionEvent.Start callback : starts) {
            callback.onConfigurationStart(handler, client);
        }
    });

    /**
     * An event called after the ReadyS2CPacket has been received, just before switching to the PLAY state.
     *
     * <p>No packets should be sent when this event is invoked.
     */
    public static final Event<Complete> COMPLETE = Event.of(completes -> (handler, client) -> {
        for (ClientConfigurationConnectionEvent.Complete callback : completes) {
            callback.onConfigurationComplete(handler, client);
        }
    });

    /**
     * An event for the disconnection of the client configuration network handler.
     *
     * <p>No packets should be sent when this event is invoked.
     */
    public static final Event<Disconnect> DISCONNECT = Event.of(disconnects -> (handler, client) -> {
        for (ClientConfigurationConnectionEvent.Disconnect callback : disconnects) {
            callback.onConfigurationDisconnect(handler, client);
        }
    });

    @FunctionalInterface
    public interface Init {
        void onConfigurationInit(ClientConfigurationNetworkHandler handler, MinecraftClient client);
    }

    @FunctionalInterface
    public interface Start {
        void onConfigurationStart(ClientConfigurationNetworkHandler handler, MinecraftClient client);
    }

    @FunctionalInterface
    public interface Complete {
        void onConfigurationComplete(ClientConfigurationNetworkHandler handler, MinecraftClient client);
    }

    @FunctionalInterface
    public interface Disconnect {
        void onConfigurationDisconnect(ClientConfigurationNetworkHandler handler, MinecraftClient client);
    }
}
