package band.kessoku.lib.impl.networking.server;

import java.util.Objects;

import net.minecraft.network.NetworkPhase;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.listener.ClientCommonPacketListener;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerConfigurationNetworkHandler;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;

import band.kessoku.lib.api.networking.server.ServerConfigurationNetworking;
import band.kessoku.lib.api.networking.server.ServerLoginNetworking;
import band.kessoku.lib.api.networking.server.ServerPlayNetworking;
import band.kessoku.lib.impl.networking.GlobalReceiverRegistry;
import band.kessoku.lib.impl.networking.NetworkHandlerExtension;
import band.kessoku.lib.impl.networking.PayloadTypeRegistryImpl;

public final class ServerNetworkingImpl {
    public static final GlobalReceiverRegistry<ServerLoginNetworking.LoginQueryResponseHandler> LOGIN = new GlobalReceiverRegistry<>(NetworkSide.SERVERBOUND, NetworkPhase.LOGIN, null);
    public static final GlobalReceiverRegistry<ServerConfigurationNetworking.ConfigurationPacketHandler<?>> CONFIG = new GlobalReceiverRegistry<>(NetworkSide.SERVERBOUND, NetworkPhase.CONFIGURATION, PayloadTypeRegistryImpl.CONFIG_C2S);
    public static final GlobalReceiverRegistry<ServerPlayNetworking.PlayPayloadHandler<?>> PLAY = new GlobalReceiverRegistry<>(NetworkSide.SERVERBOUND, NetworkPhase.PLAY, PayloadTypeRegistryImpl.PLAY_C2S);

    public static ServerPlayNetworkAddon getAddon(ServerPlayNetworkHandler handler) {
        return (ServerPlayNetworkAddon) ((NetworkHandlerExtension) handler).kessokulib$getNetworkAddon();
    }

    public static ServerLoginNetworkAddon getAddon(ServerLoginNetworkHandler handler) {
        return (ServerLoginNetworkAddon) ((NetworkHandlerExtension) handler).kessokulib$getNetworkAddon();
    }

    public static ServerConfigurationNetworkAddon getAddon(ServerConfigurationNetworkHandler handler) {
        return (ServerConfigurationNetworkAddon) ((NetworkHandlerExtension) handler).kessokulib$getNetworkAddon();
    }

    public static Packet<ClientCommonPacketListener> createS2CPacket(CustomPayload payload) {
        Objects.requireNonNull(payload, "Payload cannot be null");
        Objects.requireNonNull(payload.getId(), "CustomPayload#getId() cannot return null for payload class: " + payload.getClass());

        return new CustomPayloadS2CPacket(payload);
    }
}
