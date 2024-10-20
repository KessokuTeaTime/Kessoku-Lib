package band.kessoku.lib.api.networking;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;

/**
 * Represents something that supports sending packets to channels.
 * Any packets sent must be {@linkplain PayloadTypeRegistry registered} in the appropriate registry.
 */
@ApiStatus.NonExtendable
public interface PacketSender {
    /**
     * Creates a packet from a packet payload.
     *
     * @param payload the packet payload
     */
    Packet<?> createPacket(CustomPayload payload);

    /**
     * Sends a packet.
     *
     * @param packet the packet
     */
    default void sendPacket(Packet<?> packet) {
        sendPacket(packet, null);
    }

    /**
     * Sends a packet.
     * @param payload the payload
     */
    default void sendPacket(CustomPayload payload) {
        sendPacket(createPacket(payload));
    }

    /**
     * Sends a packet.
     *
     * @param packet the packet
     * @param callback an optional callback to execute after the packet is sent, may be {@code null}.
     */
    void sendPacket(Packet<?> packet, @Nullable PacketCallbacks callback);

    /**
     * Sends a packet.
     *
     * @param payload the payload
     * @param callback an optional callback to execute after the packet is sent, may be {@code null}.
     */
    default void sendPacket(CustomPayload payload, @Nullable PacketCallbacks callback) {
        sendPacket(createPacket(payload), callback);
    }

    /**
     * Disconnects the player.
     * @param disconnectReason the reason for disconnection
     */
    void disconnect(Text disconnectReason);
}
