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

import java.util.List;

import band.kessoku.lib.api.networking.PacketSender;
import band.kessoku.lib.event.api.Event;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;

/**
 * Offers access to events related to the indication of a connected client's ability to receive packets in certain channels.
 */
public final class S2CPlayChannelEvent {
    /**
     * An event for the server play network handler receiving an update indicating the connected client's ability to receive packets in certain channels.
     * This event may be invoked at any time after login and up to disconnection.
     */
    public static final Event<Register> REGISTER = Event.of(registers -> (handler, sender, server, channels) -> {
        for (Register callback : registers) {
            callback.onChannelRegister(handler, sender, server, channels);
        }
    });

    /**
     * An event for the server play network handler receiving an update indicating the connected client's lack of ability to receive packets in certain channels.
     * This event may be invoked at any time after login and up to disconnection.
     */
    public static final Event<Unregister> UNREGISTER = Event.of(unregisters -> (handler, sender, server, channels) -> {
        for (Unregister callback : unregisters) {
            callback.onChannelUnregister(handler, sender, server, channels);
        }
    });

    /**
     * @see S2CPlayChannelEvent#REGISTER
     */
    @FunctionalInterface
    public interface Register {
        void onChannelRegister(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server, List<Identifier> channels);
    }

    /**
     * @see S2CPlayChannelEvent#UNREGISTER
     */
    @FunctionalInterface
    public interface Unregister {
        void onChannelUnregister(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server, List<Identifier> channels);
    }
}