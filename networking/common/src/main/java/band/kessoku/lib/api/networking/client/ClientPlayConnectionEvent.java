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

import band.kessoku.lib.api.networking.PacketSender;
import band.kessoku.lib.event.api.Event;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.CustomPayload;

/**
 * Offers access to events related to the connection to a server on a logical client.
 */
public final class ClientPlayConnectionEvent {
    /**
     * Event indicating a connection entered the PLAY state, ready for registering channel handlers.
     *
     * @see ClientPlayNetworking#registerReceiver(CustomPayload.Id, ClientPlayNetworking.PlayPayloadHandler)
     */
    public static final Event<Init> INIT = Event.of(inits -> (handler, client) -> {
        for (Init callback : inits) {
            callback.onPlayInit(handler, client);
        }
    });

    /**
     * An event for notification when the client play network handler is ready to send packets to the server.
     *
     * <p>At this stage, the network handler is ready to send packets to the server.
     * Since the client's local state has been set up.
     */
    public static final Event<Join> JOIN = Event.of(joins -> (handler, sender, client) -> {
        for (Join callback : joins) {
            callback.onPlayReady(handler, sender, client);
        }
    });

    /**
     * An event for the disconnection of the client play network handler.
     *
     * <p>No packets should be sent when this event is invoked.
     */
    public static final Event<Disconnect> DISCONNECT = Event.of(disconnects -> (handler, client) -> {
        for (Disconnect callback : disconnects) {
            callback.onPlayDisconnect(handler, client);
        }
    });

    @FunctionalInterface
    public interface Init {
        void onPlayInit(ClientPlayNetworkHandler handler, MinecraftClient client);
    }

    @FunctionalInterface
    public interface Join {
        void onPlayReady(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client);
    }

    @FunctionalInterface
    public interface Disconnect {
        void onPlayDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client);
    }
}
