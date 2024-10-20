package band.kessoku.lib.impl.networking.payload;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.login.LoginQueryResponsePayload;

public record PacketByteBufLoginQueryResponsePayload(PacketByteBuf data) implements LoginQueryResponsePayload {
    @Override
    public void write(PacketByteBuf buf) {
        PayloadHelper.write(buf, data());
    }
}
