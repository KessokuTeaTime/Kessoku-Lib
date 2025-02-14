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

import java.util.List;

import band.kessoku.lib.api.networking.PacketSender;
import band.kessoku.lib.event.api.Event;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientConfigurationNetworkHandler;
import net.minecraft.util.Identifier;

/**
 * Offers access to events related to the indication of a connected server's ability to receive packets in certain channels.
 */
public final class C2SConfigurationChannelEvent {
    /**
     * An event for the client configuration network handler receiving an update indicating the connected server's ability to receive packets in certain channels.
     * This event may be invoked at any time after login and up to disconnection.
     */
    public static final Event<Register> REGISTER = Event.of(registers -> (handler, sender, client, channels) -> {
        for (Register callback : registers) {
            callback.onChannelRegister(handler, sender, client, channels);
        }
    });

    /**
     * An event for the client configuration network handler receiving an update indicating the connected server's lack of ability to receive packets in certain channels.
     * This event may be invoked at any time after login and up to disconnection.
     */
    public static final Event<Unregister> UNREGISTER = Event.of(unregisters -> (handler, sender, client, channels) -> {
        for (Unregister callback : unregisters) {
            callback.onChannelUnregister(handler, sender, client, channels);
        }
    });

    /**
     * @see C2SConfigurationChannelEvent#REGISTER
     */
    @FunctionalInterface
    public interface Register {
        void onChannelRegister(ClientConfigurationNetworkHandler handler, PacketSender sender, MinecraftClient client, List<Identifier> channels);
    }

    /**
     * @see C2SConfigurationChannelEvent#UNREGISTER
     */
    @FunctionalInterface
    public interface Unregister {
        void onChannelUnregister(ClientConfigurationNetworkHandler handler, PacketSender sender, MinecraftClient client, List<Identifier> channels);
    }
}
