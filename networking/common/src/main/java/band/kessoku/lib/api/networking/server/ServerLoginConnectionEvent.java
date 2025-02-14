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

import band.kessoku.lib.api.networking.LoginPacketSender;
import band.kessoku.lib.event.api.Event;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.util.Identifier;

/**
 * Offers access to events related to the connection to a client on a logical server while a client is logging in.
 */
public final class ServerLoginConnectionEvent {
    /**
     * Event indicating a connection entered the LOGIN state, ready for registering query response handlers.
     *
     * @see ServerLoginNetworking#registerReceiver(ServerLoginNetworkHandler, Identifier, ServerLoginNetworking.LoginQueryResponseHandler)
     */
    public static final Event<Init> INIT = Event.of(inits -> (handler, server) -> {
        for (Init callback : inits) {
            callback.onLoginInit(handler, server);
        }
    });

    /**
     * An event for the start of login queries of the server login network handler.
     * This event may be used to register {@link ServerLoginNetworking.LoginQueryResponseHandler login query response handlers}
     * using {@link ServerLoginNetworking#registerReceiver(ServerLoginNetworkHandler, Identifier, ServerLoginNetworking.LoginQueryResponseHandler)}
     * since this event is fired just before the first login query response is processed.
     *
     * <p>You may send login queries to the connected client using the provided {@link LoginPacketSender}.
     */
    public static final Event<QueryStart> QUERY_START = Event.of(queryStarts -> (handler, server, sender, synchronizer) -> {
        for (QueryStart callback : queryStarts) {
            callback.onLoginStart(handler, server, sender, synchronizer);
        }
    });

    /**
     * An event for the disconnection of the server login network handler.
     *
     * <p>No packets should be sent when this event is invoked.
     */
    public static final Event<Disconnect> DISCONNECT = Event.of(disconnects -> (handler, server) -> {
        for (Disconnect callback : disconnects) {
            callback.onLoginDisconnect(handler, server);
        }
    });

    /**
     * @see ServerLoginConnectionEvent#INIT
     */
    @FunctionalInterface
    public interface Init {
        void onLoginInit(ServerLoginNetworkHandler handler, MinecraftServer server);
    }

    /**
     * @see ServerLoginConnectionEvent#QUERY_START
     */
    @FunctionalInterface
    public interface QueryStart {
        void onLoginStart(ServerLoginNetworkHandler handler, MinecraftServer server, LoginPacketSender sender, ServerLoginNetworking.LoginSynchronizer synchronizer);
    }

    /**
     * @see ServerLoginConnectionEvent#DISCONNECT
     */
    @FunctionalInterface
    public interface Disconnect {
        void onLoginDisconnect(ServerLoginNetworkHandler handler, MinecraftServer server);
    }
}
