package band.kessoku.lib.impl.networking.common;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record CommonVersionPayload(int[] versions) implements CustomPayload {
    public static final PacketCodec<PacketByteBuf, CommonVersionPayload> CODEC = CustomPayload.codecOf(CommonVersionPayload::write, CommonVersionPayload::new);
    public static final CustomPayload.Id<CommonVersionPayload> ID = new Id<>(Identifier.of("c:version"));

    private CommonVersionPayload(PacketByteBuf buf) {
        this(buf.readIntArray());
    }

    public void write(PacketByteBuf buf) {
        buf.writeIntArray(versions);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
