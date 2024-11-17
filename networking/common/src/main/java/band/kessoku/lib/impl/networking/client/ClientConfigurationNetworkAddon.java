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

import java.util.List;
import java.util.Objects;

import band.kessoku.lib.api.networking.PacketSender;
import band.kessoku.lib.api.networking.client.C2SConfigurationChannelEvent;
import band.kessoku.lib.api.networking.client.ClientConfigurationConnectionEvent;
import band.kessoku.lib.api.networking.client.ClientConfigurationNetworking;
import band.kessoku.lib.api.networking.client.ClientPlayNetworking;
import band.kessoku.lib.impl.networking.ChannelInfoHolder;
import band.kessoku.lib.impl.networking.RegistrationPayload;
import band.kessoku.lib.mixin.networking.accessor.client.ClientCommonNetworkHandlerAccessor;
import band.kessoku.lib.mixin.networking.accessor.client.ClientConfigurationNetworkHandlerAccessor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientConfigurationNetworkHandler;
import net.minecraft.network.NetworkPhase;
import net.minecraft.network.packet.BrandCustomPayload;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Identifier;

public final class ClientConfigurationNetworkAddon extends ClientCommonNetworkAddon<ClientConfigurationNetworking.ConfigurationPayloadHandler<?>, ClientConfigurationNetworkHandler> {
    private final ContextImpl context;
    private boolean sentInitialRegisterPacket;
    private boolean hasStarted;

    public ClientConfigurationNetworkAddon(ClientConfigurationNetworkHandler handler, MinecraftClient client) {
        super(ClientNetworkingImpl.CONFIG, ((ClientCommonNetworkHandlerAccessor) handler).getConnection(), "ClientPlayNetworkAddon for " + ((ClientConfigurationNetworkHandlerAccessor) handler).getProfile().getName(), handler, client);
        this.context = new ContextImpl(client, handler, this);

        // Must register pending channels via lateinit
        this.registerPendingChannels((ChannelInfoHolder) this.connection, NetworkPhase.CONFIGURATION);
    }

    @Override
    protected void invokeInitEvent() {
        ClientConfigurationConnectionEvent.INIT.invoker().onConfigurationInit(this.handler, this.client);
    }

    @Override
    public void onServerReady() {
        super.onServerReady();
        invokeStartEvent();
    }

    @Override
    protected void receiveRegistration(boolean register, RegistrationPayload payload) {
        super.receiveRegistration(register, payload);

        if (register && !this.sentInitialRegisterPacket) {
            this.sendInitialChannelRegistrationPacket();
            this.sentInitialRegisterPacket = true;

            this.onServerReady();
        }
    }

    @Override
    public boolean handle(CustomPayload payload) {
        boolean result = super.handle(payload);

        if (payload instanceof BrandCustomPayload) {
            // If we have received this without first receiving the registration packet, its likely a vanilla server.
            invokeStartEvent();
        }

        return result;
    }

    private void invokeStartEvent() {
        if (!hasStarted) {
            hasStarted = true;
            ClientConfigurationConnectionEvent.START.invoker().onConfigurationStart(this.handler, this.client);
        }
    }

    @Override
    protected void receive(ClientConfigurationNetworking.ConfigurationPayloadHandler<?> handler, CustomPayload payload) {
        ((ClientConfigurationNetworking.ConfigurationPayloadHandler) handler).receive(payload, this.context);
    }

    // impl details
    @Override
    public Packet<?> createPacket(CustomPayload packet) {
        return ClientPlayNetworking.createC2SPacket(packet);
    }

    @Override
    protected void invokeRegisterEvent(List<Identifier> ids) {
        C2SConfigurationChannelEvent.REGISTER.invoker().onChannelRegister(this.handler, this, this.client, ids);
    }

    @Override
    protected void invokeUnregisterEvent(List<Identifier> ids) {
        C2SConfigurationChannelEvent.UNREGISTER.invoker().onChannelUnregister(this.handler, this, this.client, ids);
    }

    public void handleComplete() {
        ClientConfigurationConnectionEvent.COMPLETE.invoker().onConfigurationComplete(this.handler, this.client);
        ClientNetworkingImpl.setClientConfigurationAddon(null);
    }

    @Override
    protected void invokeDisconnectEvent() {
        ClientConfigurationConnectionEvent.DISCONNECT.invoker().onConfigurationDisconnect(this.handler, this.client);
    }

    public ChannelInfoHolder getChannelInfoHolder() {
        return (ChannelInfoHolder) ((ClientCommonNetworkHandlerAccessor) handler).getConnection();
    }

    private record ContextImpl(MinecraftClient client, ClientConfigurationNetworkHandler networkHandler, PacketSender responseSender) implements ClientConfigurationNetworking.Context {
        private ContextImpl {
            Objects.requireNonNull(client, "client");
            Objects.requireNonNull(networkHandler, "networkHandler");
            Objects.requireNonNull(responseSender, "responseSender");
        }
    }
}
