package band.kessoku.lib.impl.networking.neoforge;

import band.kessoku.lib.api.KessokuLib;
import band.kessoku.lib.api.KessokuNetworking;
import band.kessoku.lib.api.networking.PayloadTypeRegistry;
import band.kessoku.lib.api.networking.ServerConfigurationNetworkHandlerExtension;
import band.kessoku.lib.api.networking.server.ServerConfigurationConnectionEvent;
import band.kessoku.lib.api.networking.server.ServerConfigurationNetworking;
import band.kessoku.lib.impl.networking.PayloadTypeRegistryImpl;
import band.kessoku.lib.impl.networking.common.CommonPacketsImpl;
import band.kessoku.lib.impl.networking.common.CommonRegisterPayload;
import band.kessoku.lib.impl.networking.common.CommonVersionPayload;
import band.kessoku.lib.impl.networking.server.ServerConfigurationNetworkAddon;
import band.kessoku.lib.impl.networking.server.ServerNetworkingImpl;
import net.minecraft.network.NetworkPhase;
import net.minecraft.network.NetworkSide;
import net.minecraft.util.Identifier;

import static band.kessoku.lib.impl.networking.GlobalReceiverRegistry.DEFAULT_CHANNEL_NAME_MAX_LENGTH;

public class CommonPacketsImplNeoForge extends CommonPacketsImpl {
    public static void init() {
        PayloadTypeRegistry.configC2S().register(CommonVersionPayload.ID, CommonVersionPayload.CODEC);
        PayloadTypeRegistry.configS2C().register(CommonVersionPayload.ID, CommonVersionPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(CommonVersionPayload.ID, CommonVersionPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(CommonVersionPayload.ID, CommonVersionPayload.CODEC);
        PayloadTypeRegistry.configC2S().register(CommonRegisterPayload.ID, CommonRegisterPayload.CODEC);
        PayloadTypeRegistry.configS2C().register(CommonRegisterPayload.ID, CommonRegisterPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(CommonRegisterPayload.ID, CommonRegisterPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(CommonRegisterPayload.ID, CommonRegisterPayload.CODEC);

        ServerConfigurationNetworking.registerGlobalReceiver(CommonVersionPayload.ID, (payload, context) -> {
            ServerConfigurationNetworkAddon addon = ServerNetworkingImpl.getAddon(context.networkHandler());
            addon.kessokulib$onCommonVersionPacket(getNegotiatedVersion(payload));
            ((ServerConfigurationNetworkHandlerExtension) context.networkHandler()).kessokulib$completeTask(CommonPacketsImpl.CommonVersionConfigurationTask.KEY);
        });

        ServerConfigurationNetworking.registerGlobalReceiver(CommonRegisterPayload.ID, (payload, context) -> {
            ServerConfigurationNetworkAddon addon = ServerNetworkingImpl.getAddon(context.networkHandler());

            if (CommonRegisterPayload.PLAY_PHASE.equals(payload.phase())) {
                if (payload.version() != addon.kessokulib$getNegotiatedVersion()) {
                    throw new IllegalStateException("Negotiated common packet version: %d but received packet with version: %d".formatted(addon.kessokulib$getNegotiatedVersion(), payload.version()));
                }

                // Play phase hasnt started yet, add them to the pending names.
                addon.getChannelInfoHolder().kessokulib$getPendingChannelsNames(NetworkPhase.PLAY).addAll(payload.channels());
                KessokuLib.getLogger().debug(KessokuNetworking.MARKER, "Received accepted channels from the client for play phase");
            } else {
                addon.kessokulib$onCommonRegisterPacket(payload);
            }

            ((ServerConfigurationNetworkHandlerExtension) context.networkHandler()).kessokulib$completeTask(CommonPacketsImpl.CommonRegisterConfigurationTask.KEY);
        });

        // Create a configuration task to send and receive the common packets
        ServerConfigurationConnectionEvent.CONFIGURE.register((handler, server) -> {
            final ServerConfigurationNetworkAddon addon = ServerNetworkingImpl.getAddon(handler);

            if (ServerConfigurationNetworking.canSend(handler, CommonVersionPayload.ID)) {
                // Tasks are processed in order.
                ((ServerConfigurationNetworkHandlerExtension) handler).kessokulib$addTask(new CommonPacketsImpl.CommonVersionConfigurationTask(addon));

                if (ServerConfigurationNetworking.canSend(handler, CommonRegisterPayload.ID)) {
                    ((ServerConfigurationNetworkHandlerExtension) handler).kessokulib$addTask(new CommonPacketsImpl.CommonRegisterConfigurationTask(addon));
                }
            }
        });
    }

    public static void assertPayloadType(PayloadTypeRegistryImpl<?> payloadTypeRegistry, Identifier channelName, NetworkSide side, NetworkPhase phase) {
        if (payloadTypeRegistry == null) {
            return;
        }

        if (payloadTypeRegistry.get(channelName) == null) {
            throw new IllegalArgumentException(String.format("Cannot register handler as no payload type has been registered with name \"%s\" for %s %s", channelName, side, phase));
        }

        if (channelName.toString().length() > DEFAULT_CHANNEL_NAME_MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("Cannot register handler for channel with name \"%s\" as it exceeds the maximum length of 128 characters", channelName));
        }
    }
}
