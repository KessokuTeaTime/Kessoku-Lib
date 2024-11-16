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
package band.kessoku.lib.impl.networking.client;

import java.util.Collections;

import band.kessoku.lib.impl.networking.AbstractChanneledNetworkAddon;
import band.kessoku.lib.impl.networking.GlobalReceiverRegistry;
import band.kessoku.lib.impl.networking.NetworkingImpl;
import band.kessoku.lib.impl.networking.RegistrationPayload;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.util.Identifier;

abstract class ClientCommonNetworkAddon<H, T extends ClientCommonNetworkHandler> extends AbstractChanneledNetworkAddon<H> {
    protected final T handler;
    protected final MinecraftClient client;

    protected boolean isServerReady = false;

    protected ClientCommonNetworkAddon(GlobalReceiverRegistry<H> receiver, ClientConnection connection, String description, T handler, MinecraftClient client) {
        super(receiver, connection, description);
        this.handler = handler;
        this.client = client;
    }

    public void onServerReady() {
        this.isServerReady = true;
    }

    @Override
    protected void handleRegistration(Identifier channelName) {
        // If we can already send packets, immediately send the register packet for this channel
        if (this.isServerReady) {
            final RegistrationPayload payload = this.createRegistrationPayload(RegistrationPayload.REGISTER, Collections.singleton(channelName));

            if (payload != null) {
                this.sendPacket(payload);
            }
        }
    }

    @Override
    protected void handleUnregistration(Identifier channelName) {
        // If we can already send packets, immediately send the unregister packet for this channel
        if (this.isServerReady) {
            final RegistrationPayload payload = this.createRegistrationPayload(RegistrationPayload.UNREGISTER, Collections.singleton(channelName));

            if (payload != null) {
                this.sendPacket(payload);
            }
        }
    }

    @Override
    protected boolean isReservedChannel(Identifier channelName) {
        return NetworkingImpl.isReservedCommonChannel(channelName);
    }

    @Override
    protected void schedule(Runnable task) {
        client.execute(task);
    }
}
