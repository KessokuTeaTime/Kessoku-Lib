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
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;

import band.kessoku.lib.impl.networking.CustomPayloadPacketCodecExtension;
import band.kessoku.lib.impl.networking.PayloadTypeRegistryImpl;

@Mixin(CustomPayloadC2SPacket.class)
public class CustomPayloadC2SPacketMixin {
    @WrapOperation(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/packet/CustomPayload;createCodec(Lnet/minecraft/network/packet/CustomPayload$CodecFactory;Ljava/util/List;)Lnet/minecraft/network/codec/PacketCodec;"
            )
    )
    private static PacketCodec<PacketByteBuf, CustomPayload> wrapCodec(CustomPayload.CodecFactory<PacketByteBuf> unknownCodecFactory, List<CustomPayload.Type<PacketByteBuf, ?>> types, Operation<PacketCodec<PacketByteBuf, CustomPayload>> original) {
        PacketCodec<PacketByteBuf, CustomPayload> codec = original.call(unknownCodecFactory, types);
        CustomPayloadPacketCodecExtension<PacketByteBuf> kessokuCodec = (CustomPayloadPacketCodecExtension<PacketByteBuf>) codec;
        kessokuCodec.kessokulib$setPacketCodecProvider((packetByteBuf, identifier) -> {
            // CustomPayloadC2SPacket does not have a separate codec for play/configuration. We know if the packetByteBuf is a PacketByteBuf we are in the play phase.
            if (packetByteBuf instanceof RegistryByteBuf) {
                return (CustomPayload.Type<PacketByteBuf, ? extends CustomPayload>) (Object) PayloadTypeRegistryImpl.PLAY_C2S.get(identifier);
            }

            return PayloadTypeRegistryImpl.CONFIG_C2S.get(identifier);
        });
        return codec;
    }
}
