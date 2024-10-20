package band.kessoku.lib.impl.networking.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.c2s.login.LoginQueryResponseC2SPacket;
import net.minecraft.network.packet.s2c.login.LoginQueryRequestS2CPacket;
import net.minecraft.util.Identifier;

import band.kessoku.lib.api.networking.client.ClientLoginConnectionEvent;
import band.kessoku.lib.api.networking.client.ClientLoginNetworking;
import band.kessoku.lib.api.networking.PacketByteBufHelper;
import band.kessoku.lib.impl.networking.AbstractNetworkAddon;
import band.kessoku.lib.impl.networking.payload.PacketByteBufLoginQueryRequestPayload;
import band.kessoku.lib.impl.networking.payload.PacketByteBufLoginQueryResponsePayload;
import band.kessoku.lib.mixin.networking.accessor.client.ClientLoginNetworkHandlerAccessor;

public final class ClientLoginNetworkAddon extends AbstractNetworkAddon<ClientLoginNetworking.LoginQueryRequestHandler> {
    private final ClientLoginNetworkHandler handler;
    private final MinecraftClient client;
    private boolean firstResponse = true;

    public ClientLoginNetworkAddon(ClientLoginNetworkHandler handler, MinecraftClient client) {
        super(ClientNetworkingImpl.LOGIN, "ClientLoginNetworkAddon for Client");
        this.handler = handler;
        this.client = client;
    }

    @Override
    protected void invokeInitEvent() {
        ClientLoginConnectionEvent.INIT.invoker().onLoginStart(this.handler, this.client);
    }

    public boolean handlePacket(LoginQueryRequestS2CPacket packet) {
        PacketByteBufLoginQueryRequestPayload payload = (PacketByteBufLoginQueryRequestPayload) packet.payload();
        return handlePacket(packet.queryId(), packet.payload().id(), payload.data());
    }

    private boolean handlePacket(int queryId, Identifier channelName, PacketByteBuf originalBuf) {
        this.logger.debug("Handling inbound login response with id {} and channel with name {}", queryId, channelName);

        if (this.firstResponse) {
            ClientLoginConnectionEvent.QUERY_START.invoker().onLoginQueryStart(this.handler, this.client);
            this.firstResponse = false;
        }

        @Nullable ClientLoginNetworking.LoginQueryRequestHandler handler = this.getHandler(channelName);

        if (handler == null) {
            return false;
        }

        PacketByteBuf buf = PacketByteBufHelper.slice(originalBuf);
        List<PacketCallbacks> callbacks = new ArrayList<>();

        try {
            CompletableFuture<@Nullable PacketByteBuf> future = handler.receive(this.client, this.handler, buf, callbacks::add);
            future.thenAccept(result -> {
                LoginQueryResponseC2SPacket packet = new LoginQueryResponseC2SPacket(queryId, result == null ? null : new PacketByteBufLoginQueryResponsePayload(result));
                ((ClientLoginNetworkHandlerAccessor) this.handler).getConnection().send(packet, new PacketCallbacks() {
                    @Override
                    public void onSuccess() {
                        callbacks.forEach(PacketCallbacks::onSuccess);
                    }
                });
            });
        } catch (Throwable ex) {
            this.logger.error("Encountered exception while handling in channel with name \"{}\"", channelName, ex);
            throw ex;
        }

        return true;
    }

    @Override
    protected void handleRegistration(Identifier channelName) {
    }

    @Override
    protected void handleUnregistration(Identifier channelName) {
    }

    @Override
    protected void invokeDisconnectEvent() {
        ClientLoginConnectionEvent.DISCONNECT.invoker().onLoginDisconnect(this.handler, this.client);
    }

    @Override
    protected boolean isReservedChannel(Identifier channelName) {
        return false;
    }
}
