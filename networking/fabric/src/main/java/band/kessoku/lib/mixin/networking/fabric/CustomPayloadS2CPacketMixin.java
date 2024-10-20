package band.kessoku.lib.mixin.networking.fabric;

import java.util.List;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;

import band.kessoku.lib.impl.networking.CustomPayloadPacketCodecExtension;
import band.kessoku.lib.impl.networking.PayloadTypeRegistryImpl;

@Mixin(CustomPayloadS2CPacket.class)
public class CustomPayloadS2CPacketMixin {
    @WrapOperation(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/packet/CustomPayload;createCodec(Lnet/minecraft/network/packet/CustomPayload$CodecFactory;Ljava/util/List;)Lnet/minecraft/network/codec/PacketCodec;",
                    ordinal = 0
            )
    )
    private static PacketCodec<RegistryByteBuf, CustomPayload> wrapPlayCodec(CustomPayload.CodecFactory<RegistryByteBuf> unknownCodecFactory, List<CustomPayload.Type<RegistryByteBuf, ?>> types, Operation<PacketCodec<RegistryByteBuf, CustomPayload>> original) {
        PacketCodec<RegistryByteBuf, CustomPayload> codec = original.call(unknownCodecFactory, types);
        CustomPayloadPacketCodecExtension<RegistryByteBuf> kessokuCodec = (CustomPayloadPacketCodecExtension<RegistryByteBuf>) codec;
        kessokuCodec.kessokulib$setPacketCodecProvider((packetByteBuf, identifier) -> PayloadTypeRegistryImpl.PLAY_S2C.get(identifier));
        return codec;
    }

    @WrapOperation(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/packet/CustomPayload;createCodec(Lnet/minecraft/network/packet/CustomPayload$CodecFactory;Ljava/util/List;)Lnet/minecraft/network/codec/PacketCodec;",
                    ordinal = 1
            )
    )
    private static PacketCodec<PacketByteBuf, CustomPayload> wrapConfigCodec(CustomPayload.CodecFactory<PacketByteBuf> unknownCodecFactory, List<CustomPayload.Type<PacketByteBuf, ?>> types, Operation<PacketCodec<PacketByteBuf, CustomPayload>> original) {
        PacketCodec<PacketByteBuf, CustomPayload> codec = original.call(unknownCodecFactory, types);
        CustomPayloadPacketCodecExtension<PacketByteBuf> kessokuCodec = (CustomPayloadPacketCodecExtension<PacketByteBuf>) codec;
        kessokuCodec.kessokulib$setPacketCodecProvider((packetByteBuf, identifier) -> PayloadTypeRegistryImpl.CONFIG_S2C.get(identifier));
        return codec;
    }
}
