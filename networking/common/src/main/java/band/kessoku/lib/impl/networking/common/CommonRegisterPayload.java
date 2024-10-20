package band.kessoku.lib.impl.networking.common;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record CommonRegisterPayload(int version, String phase, Set<Identifier> channels) implements CustomPayload {
    public static final CustomPayload.Id<CommonRegisterPayload> ID = new Id<>(Identifier.of("c:register"));
    public static final PacketCodec<PacketByteBuf, CommonRegisterPayload> CODEC = CustomPayload.codecOf(CommonRegisterPayload::write, CommonRegisterPayload::new);

    public static final String PLAY_PHASE = "play";
    public static final String CONFIGURATION_PHASE = "configuration";

    private CommonRegisterPayload(PacketByteBuf buf) {
        this(
                buf.readVarInt(),
                buf.readString(),
                buf.readCollection(HashSet::new, PacketByteBuf::readIdentifier)
        );
    }

    public void write(PacketByteBuf buf) {
        buf.writeVarInt(version);
        buf.writeString(phase);
        buf.writeCollection(channels, PacketByteBuf::writeIdentifier);
    }

    @Override
    public Id<CommonRegisterPayload> getId() {
        return ID;
    }
}
