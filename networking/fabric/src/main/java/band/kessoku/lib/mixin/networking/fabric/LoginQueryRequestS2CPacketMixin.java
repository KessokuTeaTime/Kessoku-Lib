package band.kessoku.lib.mixin.networking.fabric;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.login.LoginQueryRequestPayload;
import net.minecraft.network.packet.s2c.login.LoginQueryRequestS2CPacket;
import net.minecraft.util.Identifier;

import band.kessoku.lib.impl.networking.payload.PacketByteBufLoginQueryRequestPayload;
import band.kessoku.lib.impl.networking.payload.PayloadHelper;

@Mixin(LoginQueryRequestS2CPacket.class)
public class LoginQueryRequestS2CPacketMixin {
    @Shadow
    @Final
    private static int MAX_PAYLOAD_SIZE;

    @Inject(method = "readPayload", at = @At("HEAD"), cancellable = true)
    private static void readPayload(Identifier id, PacketByteBuf buf, CallbackInfoReturnable<LoginQueryRequestPayload> cir) {
        cir.setReturnValue(new PacketByteBufLoginQueryRequestPayload(id, PayloadHelper.read(buf, MAX_PAYLOAD_SIZE)));
    }
}
