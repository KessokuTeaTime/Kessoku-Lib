package band.kessoku.lib.impl.networking;

import net.minecraft.network.PacketByteBuf;

public interface CustomPayloadPacketCodecExtension<B extends PacketByteBuf> {
    void kessokulib$setPacketCodecProvider(CustomPayloadTypeProvider<B> customPayloadTypeProvider);
}
