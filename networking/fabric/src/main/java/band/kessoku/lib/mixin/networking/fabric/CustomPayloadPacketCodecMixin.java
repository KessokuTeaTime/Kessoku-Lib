/*
 * Copyright (c) 2024 KessokuTeaTime
 *
 * Licensed under the GNU Lesser General Pubic License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package band.kessoku.lib.mixin.networking.fabric;

import band.kessoku.lib.impl.networking.CustomPayloadPacketCodecExtension;
import band.kessoku.lib.impl.networking.CustomPayloadTypeProvider;
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
