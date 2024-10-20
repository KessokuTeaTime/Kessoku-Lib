package band.kessoku.lib.mixin.networking.fabric;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.login.LoginQueryResponseC2SPacket;
import net.minecraft.network.packet.c2s.login.LoginQueryResponsePayload;

import band.kessoku.lib.impl.networking.payload.PacketByteBufLoginQueryResponsePayload;
import band.kessoku.lib.impl.networking.payload.PayloadHelper;

@Mixin(LoginQueryResponseC2SPacket.class)
public class LoginQueryResponseC2SPacketMixin {
    @Shadow
    @Final
    private static int MAX_PAYLOAD_SIZE;

    @Inject(method = "readPayload", at = @At("HEAD"), cancellable = true)
    private static void readResponse(int queryId, PacketByteBuf buf, CallbackInfoReturnable<LoginQueryResponsePayload> cir) {
        boolean hasPayload = buf.readBoolean();

        if (!hasPayload) {
            cir.setReturnValue(null);
            return;
        }

        cir.setReturnValue(new PacketByteBufLoginQueryResponsePayload(PayloadHelper.read(buf, MAX_PAYLOAD_SIZE)));
    }
}
