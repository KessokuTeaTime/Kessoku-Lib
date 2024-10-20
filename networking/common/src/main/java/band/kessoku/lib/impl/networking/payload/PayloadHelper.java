package band.kessoku.lib.impl.networking.payload;

import net.minecraft.network.PacketByteBuf;

import band.kessoku.lib.api.networking.PacketByteBufHelper;

public class PayloadHelper {
    public static void write(PacketByteBuf byteBuf, PacketByteBuf data) {
        byteBuf.writeBytes(data.copy());
    }

    public static PacketByteBuf read(PacketByteBuf byteBuf, int maxSize) {
        assertSize(byteBuf, maxSize);

        PacketByteBuf newBuf = PacketByteBufHelper.create();
        newBuf.writeBytes(byteBuf.copy());
        byteBuf.skipBytes(byteBuf.readableBytes());
        return newBuf;
    }

    private static void assertSize(PacketByteBuf buf, int maxSize) {
        int size = buf.readableBytes();

        if (size < 0 || size > maxSize) {
            throw new IllegalArgumentException("Payload may not be larger than " + maxSize + " bytes");
        }
    }
}
