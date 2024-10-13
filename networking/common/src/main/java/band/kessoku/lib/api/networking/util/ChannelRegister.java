package band.kessoku.lib.api.networking.util;

import band.kessoku.lib.impl.networking.ChannelRegisterImpl;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.ApiStatus;

/**
 * A registry for payload types.
 */
@ApiStatus.NonExtendable
public interface ChannelRegister<B extends PacketByteBuf> {

    /**
     * Registers a custom payload type.
     *
     * <p>This must be done on both the sending and receiving side, usually during mod initialization
     * and <strong>before registering a packet handler</strong>.
     *
     * @param id    the id of the payload type
     * @param codec the codec for the payload type
     * @param <T>   the payload type
     * @return the registered payload type
     */
    <T extends CustomPayload> CustomPayload.Type<? super B, T> register(CustomPayload.Id<T> id, PacketCodec<? super B, T> codec);

    /**
     * @return the {@link ChannelRegister} instance for the client to server configuration channel.
     */
    static ChannelRegister<PacketByteBuf> configC2S() {
        return ChannelRegisterImpl.CONFIG_C2S;
    }

    /**
     * @return the {@link ChannelRegister} instance for the server to client configuration channel.
     */
    static ChannelRegister<PacketByteBuf> configS2C() {
        return ChannelRegisterImpl.CONFIG_S2C;
    }

    /**
     * @return the {@link ChannelRegister} instance for the client to server play channel.
     */
    static ChannelRegister<RegistryByteBuf> playC2S() {
        return ChannelRegisterImpl.PLAY_C2S;
    }

    /**
     * @return the {@link ChannelRegister} instance for the server to client play channel.
     */
    static ChannelRegister<RegistryByteBuf> playS2C() {
        return ChannelRegisterImpl.PLAY_S2C;
    }
}
