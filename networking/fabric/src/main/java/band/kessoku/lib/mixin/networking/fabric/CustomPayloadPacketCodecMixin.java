package band.kessoku.lib.mixin.networking.fabric;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import band.kessoku.lib.impl.networking.CustomPayloadTypeProvider;
import band.kessoku.lib.impl.networking.CustomPayloadPacketCodecExtension;

@Mixin(targets = "net/minecraft/network/packet/CustomPayload$1")
public abstract class CustomPayloadPacketCodecMixin<B extends PacketByteBuf> implements PacketCodec<B, CustomPayload>, CustomPayloadPacketCodecExtension<B> {
    @Unique
    private CustomPayloadTypeProvider<B> kessokulib$customPayloadTypeProvider;

    @Override
    public void kessokulib$setPacketCodecProvider(CustomPayloadTypeProvider<B> customPayloadTypeProvider) {
        if (this.kessokulib$customPayloadTypeProvider != null) {
            throw new IllegalStateException("Payload codec provider is already set!");
        }

        this.kessokulib$customPayloadTypeProvider = customPayloadTypeProvider;
    }

    @WrapOperation(method = {
            "encode(Lnet/minecraft/network/PacketByteBuf;Lnet/minecraft/network/packet/CustomPayload$Id;Lnet/minecraft/network/packet/CustomPayload;)V",
            "decode(Lnet/minecraft/network/PacketByteBuf;)Lnet/minecraft/network/packet/CustomPayload;"
    }, at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/CustomPayload$1;getCodec(Lnet/minecraft/util/Identifier;)Lnet/minecraft/network/codec/PacketCodec;"))
    private PacketCodec<B, ? extends CustomPayload> wrapGetCodec(@Coerce PacketCodec<B, CustomPayload> instance, Identifier identifier, Operation<PacketCodec<B, CustomPayload>> original, B packetByteBuf) {
        if (kessokulib$customPayloadTypeProvider != null) {
            CustomPayload.Type<B, ? extends CustomPayload> payloadType = kessokulib$customPayloadTypeProvider.kessokulib$get(packetByteBuf, identifier);

            if (payloadType != null) {
                return payloadType.codec();
            }
        }

        return original.call(instance, identifier);
    }
}
