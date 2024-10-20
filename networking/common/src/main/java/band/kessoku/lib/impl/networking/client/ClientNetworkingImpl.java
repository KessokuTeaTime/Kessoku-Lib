package band.kessoku.lib.impl.networking.client;

import java.util.Objects;

import band.kessoku.lib.api.KessokuLib;
import band.kessoku.lib.api.KessokuNetworking;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.ClientConfigurationNetworkHandler;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkPhase;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.listener.ServerCommonPacketListener;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;

import band.kessoku.lib.api.networking.client.ClientConfigurationConnectionEvent;
import band.kessoku.lib.api.networking.client.ClientConfigurationNetworking;
import band.kessoku.lib.api.networking.client.ClientLoginNetworking;
import band.kessoku.lib.api.networking.client.ClientPlayConnectionEvent;
import band.kessoku.lib.api.networking.client.ClientPlayNetworking;
import band.kessoku.lib.api.networking.PacketSender;
import band.kessoku.lib.impl.networking.common.CommonPacketsImpl;
import band.kessoku.lib.impl.networking.common.CommonRegisterPayload;
import band.kessoku.lib.impl.networking.common.CommonVersionPayload;
import band.kessoku.lib.impl.networking.GlobalReceiverRegistry;
import band.kessoku.lib.impl.networking.NetworkHandlerExtension;
import band.kessoku.lib.impl.networking.PayloadTypeRegistryImpl;
import band.kessoku.lib.mixin.networking.accessor.client.ConnectScreenAccessor;
import band.kessoku.lib.mixin.networking.accessor.client.MinecraftClientAccessor;

public final class ClientNetworkingImpl {
    public static final GlobalReceiverRegistry<ClientLoginNetworking.LoginQueryRequestHandler> LOGIN = new GlobalReceiverRegistry<>(NetworkSide.CLIENTBOUND, NetworkPhase.LOGIN, null);
    public static final GlobalReceiverRegistry<ClientConfigurationNetworking.ConfigurationPayloadHandler<?>> CONFIG = new GlobalReceiverRegistry<>(NetworkSide.CLIENTBOUND, NetworkPhase.CONFIGURATION, PayloadTypeRegistryImpl.CONFIG_S2C);
    public static final GlobalReceiverRegistry<ClientPlayNetworking.PlayPayloadHandler<?>> PLAY = new GlobalReceiverRegistry<>(NetworkSide.CLIENTBOUND, NetworkPhase.PLAY, PayloadTypeRegistryImpl.PLAY_S2C);

    private static ClientPlayNetworkAddon currentPlayAddon;
    private static ClientConfigurationNetworkAddon currentConfigurationAddon;

    public static ClientPlayNetworkAddon getAddon(ClientPlayNetworkHandler handler) {
        return (ClientPlayNetworkAddon) ((NetworkHandlerExtension) handler).kessokulib$getNetworkAddon();
    }

    public static ClientConfigurationNetworkAddon getAddon(ClientConfigurationNetworkHandler handler) {
        return (ClientConfigurationNetworkAddon) ((NetworkHandlerExtension) handler).kessokulib$getNetworkAddon();
    }

    public static ClientLoginNetworkAddon getAddon(ClientLoginNetworkHandler handler) {
        return (ClientLoginNetworkAddon) ((NetworkHandlerExtension) handler).kessokulib$getNetworkAddon();
    }

    public static Packet<ServerCommonPacketListener> createC2SPacket(CustomPayload payload) {
        Objects.requireNonNull(payload, "Payload cannot be null");
        Objects.requireNonNull(payload.getId(), "CustomPayload#getId() cannot return null for payload class: " + payload.getClass());

        return new CustomPayloadC2SPacket(payload);
    }

    /**
     * Due to the way logging into an integrated or remote dedicated server will differ, we need to obtain the login client connection differently.
     */
    @Nullable
    public static ClientConnection getLoginConnection() {
        final ClientConnection connection = ((MinecraftClientAccessor) MinecraftClient.getInstance()).getConnection();

        // Check if we are connecting to an integrated server. This will set the field on MinecraftClient
        if (connection != null) {
            return connection;
        } else {
            // We are probably connecting to a remote server.
            // Check if the ConnectScreen is the currentScreen to determine that:
            if (MinecraftClient.getInstance().currentScreen instanceof ConnectScreen) {
                return ((ConnectScreenAccessor) MinecraftClient.getInstance().currentScreen).getConnection();
            }
        }

        // We are not connected to a server at all.
        return null;
    }

    @Nullable
    public static ClientConfigurationNetworkAddon getClientConfigurationAddon() {
        return currentConfigurationAddon;
    }

    @Nullable
    public static ClientPlayNetworkAddon getClientPlayAddon() {
        // Since Minecraft can be a bit weird, we need to check for the play addon in a few ways:
        // If the client's player is set this will work
        if (MinecraftClient.getInstance().getNetworkHandler() != null) {
            currentPlayAddon = null; // Shouldn't need this anymore
            return getAddon(MinecraftClient.getInstance().getNetworkHandler());
        }

        // We haven't hit the end of onGameJoin yet, use our backing field here to access the network handler
        if (currentPlayAddon != null) {
            return currentPlayAddon;
        }

        // We are not in play stage
        return null;
    }

    public static void setClientPlayAddon(ClientPlayNetworkAddon addon) {
        assert addon == null || currentConfigurationAddon == null;
        currentPlayAddon = addon;
    }

    public static void setClientConfigurationAddon(ClientConfigurationNetworkAddon addon) {
        currentConfigurationAddon = addon;
    }

    public static void clientInit() {
        // Reference cleanup for the locally stored addon if we are disconnected
        ClientPlayConnectionEvent.DISCONNECT.register((handler, client) -> {
            currentPlayAddon = null;
        });

        ClientConfigurationConnectionEvent.DISCONNECT.register((handler, client) -> {
            currentConfigurationAddon = null;
        });

        // Version packet
        ClientConfigurationNetworking.registerGlobalReceiver(CommonVersionPayload.ID, (payload, context) -> {
            int negotiatedVersion = handleVersionPacket(payload, context.responseSender());
            ClientNetworkingImpl.getClientConfigurationAddon().kessokulib$onCommonVersionPacket(negotiatedVersion);
        });

        // Register packet
        ClientConfigurationNetworking.registerGlobalReceiver(CommonRegisterPayload.ID, (payload, context) -> {
            ClientConfigurationNetworkAddon addon = ClientNetworkingImpl.getClientConfigurationAddon();

            if (CommonRegisterPayload.PLAY_PHASE.equals(payload.phase())) {
                if (payload.version() != addon.kessokulib$getNegotiatedVersion()) {
                    throw new IllegalStateException("Negotiated common packet version: %d but received packet with version: %d".formatted(addon.kessokulib$getNegotiatedVersion(), payload.version()));
                }

                addon.getChannelInfoHolder().kessokulib$getPendingChannelsNames(NetworkPhase.PLAY).addAll(payload.channels());
                KessokuLib.getLogger().debug(KessokuNetworking.MARKER, "Received accepted channels from the server");
                context.responseSender().sendPacket(new CommonRegisterPayload(addon.kessokulib$getNegotiatedVersion(), CommonRegisterPayload.PLAY_PHASE, ClientPlayNetworking.getGlobalReceivers()));
            } else {
                addon.kessokulib$onCommonRegisterPacket(payload);
                context.responseSender().sendPacket(addon.kessokulib$createRegisterPayload());
            }
        });
    }

    // Disconnect if there are no commonly supported versions.
    // Client responds with the intersection of supported versions.
    // Return the highest supported version
    private static int handleVersionPacket(CommonVersionPayload payload, PacketSender packetSender) {
        int version = CommonPacketsImpl.getHighestCommonVersion(payload.versions(), CommonPacketsImpl.SUPPORTED_COMMON_PACKET_VERSIONS);

        if (version <= 0) {
            throw new UnsupportedOperationException("Client does not support any requested versions from server");
        }

        packetSender.sendPacket(new CommonVersionPayload(new int[]{ version }));
        return version;
    }
}
