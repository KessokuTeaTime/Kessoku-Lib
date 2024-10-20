package band.kessoku.lib.api.networking;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;

import java.util.Objects;

/**
 * Helper methods for working with and creating {@link PacketByteBuf}s.
 */
public final class PacketByteBufHelper {
    private static final PacketByteBuf EMPTY_PACKET_BYTE_BUF = new PacketByteBuf(Unpooled.EMPTY_BUFFER);

    /**
     * Returns an empty instance of packet byte buf.
     *
     * @return an empty buf
     */
    public static PacketByteBuf empty() {
        return EMPTY_PACKET_BYTE_BUF;
    }

    /**
     * Returns a new heap memory-backed instance of packet byte buf.
     *
     * @return a new buf
     */
    public static PacketByteBuf create() {
        return new PacketByteBuf(Unpooled.buffer());
    }

    // Convenience methods for byte buf methods that return a new byte buf

    /**
     * Wraps the newly created buf from {@code buf.readBytes} in a packet byte buf.
     *
     * @param buf    the original buf
     * @param length the number of bytes to transfer
     * @return the transferred bytes
     * @see ByteBuf#readBytes(int)
     */
    public static PacketByteBuf readBytes(ByteBuf buf, int length) {
        Objects.requireNonNull(buf, "ByteBuf cannot be null");

        return new PacketByteBuf(buf.readBytes(length));
    }

    /**
     * Wraps the newly created buf from {@code buf.readSlice} in a packet byte buf.
     *
     * @param buf    the original buf
     * @param length the size of the new slice
     * @return the newly created slice
     * @see ByteBuf#readSlice(int)
     */
    public static PacketByteBuf readSlice(ByteBuf buf, int length) {
        Objects.requireNonNull(buf, "ByteBuf cannot be null");

        return new PacketByteBuf(buf.readSlice(length));
    }

    /**
     * Wraps the newly created buf from {@code buf.readRetainedSlice} in a packet byte buf.
     *
     * @param buf    the original buf
     * @param length the size of the new slice
     * @return the newly created slice
     * @see ByteBuf#readRetainedSlice(int)
     */
    public static PacketByteBuf readRetainedSlice(ByteBuf buf, int length) {
        Objects.requireNonNull(buf, "ByteBuf cannot be null");

        return new PacketByteBuf(buf.readRetainedSlice(length));
    }

    /**
     * Wraps the newly created buf from {@code buf.copy} in a packet byte buf.
     *
     * @param buf the original buf
     * @return a copy of the buf
     * @see ByteBuf#copy()
     */
    public static PacketByteBuf copy(ByteBuf buf) {
        Objects.requireNonNull(buf, "ByteBuf cannot be null");

        return new PacketByteBuf(buf.copy());
    }

    /**
     * Wraps the newly created buf from {@code buf.copy} in a packet byte buf.
     *
     * @param buf    the original buf
     * @param index  the starting index
     * @param length the size of the copy
     * @return a copy of the buf
     * @see ByteBuf#copy(int, int)
     */
    public static PacketByteBuf copy(ByteBuf buf, int index, int length) {
        Objects.requireNonNull(buf, "ByteBuf cannot be null");

        return new PacketByteBuf(buf.copy(index, length));
    }

    /**
     * Wraps the newly created buf from {@code buf.slice} in a packet byte buf.
     *
     * @param buf the original buf
     * @return a slice of the buf
     * @see ByteBuf#slice()
     */
    public static PacketByteBuf slice(ByteBuf buf) {
        Objects.requireNonNull(buf, "ByteBuf cannot be null");

        return new PacketByteBuf(buf.slice());
    }

    /**
     * Wraps the newly created buf from {@code buf.retainedSlice} in a packet byte buf.
     *
     * @param buf the original buf
     * @return a slice of the buf
     * @see ByteBuf#retainedSlice()
     */
    public static PacketByteBuf retainedSlice(ByteBuf buf) {
        Objects.requireNonNull(buf, "ByteBuf cannot be null");

        return new PacketByteBuf(buf.retainedSlice());
    }

    /**
     * Wraps the newly created buf from {@code buf.slice} in a packet byte buf.
     *
     * @param buf    the original buf
     * @param index  the starting index
     * @param length the size of the copy
     * @return a slice of the buf
     * @see ByteBuf#slice(int, int)
     */
    public static PacketByteBuf slice(ByteBuf buf, int index, int length) {
        Objects.requireNonNull(buf, "ByteBuf cannot be null");

        return new PacketByteBuf(buf.slice(index, length));
    }

    /**
     * Wraps the newly created buf from {@code buf.retainedSlice} in a packet byte buf.
     *
     * @param buf    the original buf
     * @param index  the starting index
     * @param length the size of the copy
     * @return a slice of the buf
     * @see ByteBuf#retainedSlice(int, int)
     */
    public static PacketByteBuf retainedSlice(ByteBuf buf, int index, int length) {
        Objects.requireNonNull(buf, "ByteBuf cannot be null");

        return new PacketByteBuf(buf.retainedSlice(index, length));
    }

    /**
     * Wraps the newly created buf from {@code buf.duplicate} in a packet byte buf.
     *
     * @param buf the original buf
     * @return a duplicate of the buf
     * @see ByteBuf#duplicate()
     */
    public static PacketByteBuf duplicate(ByteBuf buf) {
        Objects.requireNonNull(buf, "ByteBuf cannot be null");

        return new PacketByteBuf(buf.duplicate());
    }

    /**
     * Wraps the newly created buf from {@code buf.retainedDuplicate} in a packet byte buf.
     *
     * @param buf the original buf
     * @return a duplicate of the buf
     * @see ByteBuf#retainedDuplicate()
     */
    public static PacketByteBuf retainedDuplicate(ByteBuf buf) {
        Objects.requireNonNull(buf, "ByteBuf cannot be null");

        return new PacketByteBuf(buf.retainedDuplicate());
    }
}
