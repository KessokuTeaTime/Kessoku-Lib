package band.kessoku.lib.impl.networking.payload;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.login.LoginQueryRequestPayload;
import net.minecraft.util.Identifier;

public record PacketByteBufLoginQueryRequestPayload(Identifier id, PacketByteBuf data) implements LoginQueryRequestPayload {
    @Override
    public void write(PacketByteBuf buf) {
        PayloadHelper.write(buf, data());
    }
}