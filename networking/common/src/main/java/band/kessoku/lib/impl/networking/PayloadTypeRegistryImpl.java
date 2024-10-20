package band.kessoku.lib.impl.networking;

import band.kessoku.lib.api.networking.PayloadTypeRegistry;
import net.minecraft.network.NetworkPhase;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PayloadTypeRegistryImpl<B extends PacketByteBuf> implements PayloadTypeRegistry<B> {
    public static final PayloadTypeRegistryImpl<PacketByteBuf> CONFIG_C2S = new PayloadTypeRegistryImpl<>(NetworkPhase.CONFIGURATION, NetworkSide.SERVERBOUND);
    public static final PayloadTypeRegistryImpl<PacketByteBuf> CONFIG_S2C = new PayloadTypeRegistryImpl<>(NetworkPhase.CONFIGURATION, NetworkSide.CLIENTBOUND);
    public static final PayloadTypeRegistryImpl<RegistryByteBuf> PLAY_C2S = new PayloadTypeRegistryImpl<>(NetworkPhase.PLAY, NetworkSide.SERVERBOUND);
    public static final PayloadTypeRegistryImpl<RegistryByteBuf> PLAY_S2C = new PayloadTypeRegistryImpl<>(NetworkPhase.PLAY, NetworkSide.CLIENTBOUND);

    private final Map<Identifier, CustomPayload.Type<B, ? extends CustomPayload>> packetTypes = new HashMap<>();
    private final NetworkPhase state;
    private final NetworkSide side;

    private PayloadTypeRegistryImpl(NetworkPhase state, NetworkSide side) {
        this.state = state;
        this.side = side;
    }
    @Override
    public <T extends CustomPayload> CustomPayload.Type<? super B, T> register(CustomPayload.Id<T> id, PacketCodec<? super B, T> codec) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(codec, "codec");

        final CustomPayload.Type<B, T> payloadType = new CustomPayload.Type<>(id, codec.cast());

        if (packetTypes.containsKey(id.id())) {
            throw new IllegalArgumentException("Packet type " + id + " is already registered!");
        }

        packetTypes.put(id.id(), payloadType);
        return payloadType;
    }

    @Nullable
    public CustomPayload.Type<B, ? extends CustomPayload> get(Identifier id) {
        return packetTypes.get(id);
    }

    @Nullable
    public <T extends CustomPayload> CustomPayload.Type<B, T> get(CustomPayload.Id<T> id) {
        //noinspection unchecked
        return (CustomPayload.Type<B, T>) packetTypes.get(id.id());
    }

    public NetworkPhase getPhase() {
        return state;
    }

    public NetworkSide getSide() {
        return side;
    }
}
