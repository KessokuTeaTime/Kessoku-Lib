package band.kessoku.lib.impl.networking;

import band.kessoku.lib.api.networking.PayloadTypeRegistry;
import net.minecraft.util.Identifier;

public class NetworkingImpl {
    /**
     * Id of packet used to register supported channels.
     */
    public static final Identifier REGISTER_CHANNEL = Identifier.ofVanilla("register");

    /**
     * Id of packet used to unregister supported channels.
     */
    public static final Identifier UNREGISTER_CHANNEL = Identifier.ofVanilla("unregister");

    public static boolean isReservedCommonChannel(Identifier channelName) {
        return channelName.equals(REGISTER_CHANNEL) || channelName.equals(UNREGISTER_CHANNEL);
    }

    public static void init() {
        PayloadTypeRegistry.configS2C().register(RegistrationPayload.REGISTER, RegistrationPayload.REGISTER_CODEC);
        PayloadTypeRegistry.configS2C().register(RegistrationPayload.UNREGISTER, RegistrationPayload.UNREGISTER_CODEC);
        PayloadTypeRegistry.configC2S().register(RegistrationPayload.REGISTER, RegistrationPayload.REGISTER_CODEC);
        PayloadTypeRegistry.configC2S().register(RegistrationPayload.UNREGISTER, RegistrationPayload.UNREGISTER_CODEC);
        PayloadTypeRegistry.playS2C().register(RegistrationPayload.REGISTER, RegistrationPayload.REGISTER_CODEC);
        PayloadTypeRegistry.playS2C().register(RegistrationPayload.UNREGISTER, RegistrationPayload.UNREGISTER_CODEC);
        PayloadTypeRegistry.playC2S().register(RegistrationPayload.REGISTER, RegistrationPayload.REGISTER_CODEC);
        PayloadTypeRegistry.playC2S().register(RegistrationPayload.UNREGISTER, RegistrationPayload.UNREGISTER_CODEC);
    }
}
