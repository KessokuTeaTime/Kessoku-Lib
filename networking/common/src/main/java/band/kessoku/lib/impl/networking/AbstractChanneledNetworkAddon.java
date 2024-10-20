package band.kessoku.lib.impl.networking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import band.kessoku.lib.impl.networking.common.CommonPacketHandler;
import band.kessoku.lib.impl.networking.common.CommonRegisterPayload;
import org.jetbrains.annotations.Nullable;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkPhase;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import band.kessoku.lib.api.networking.PacketSender;

/**
 * A network addon which is aware of the channels the other side may receive.
 *
 * @param <H> the channel handler type
 */
public abstract class AbstractChanneledNetworkAddon<H> extends AbstractNetworkAddon<H> implements PacketSender, CommonPacketHandler {
    // The maximum number of channels that a connecting client can register.
    private static final int MAX_CHANNELS = Integer.getInteger("kessokulib.networking.maxChannels", 8192);
    // The maximum length of a channel name a connecting client can use, 128 is the default and minimum value.
    private static final int MAX_CHANNEL_NAME_LENGTH = Math.max(Integer.getInteger("kessokulib.networking.maxChannelNameLength", GlobalReceiverRegistry.DEFAULT_CHANNEL_NAME_MAX_LENGTH), GlobalReceiverRegistry.DEFAULT_CHANNEL_NAME_MAX_LENGTH);

    protected final ClientConnection connection;
    protected final GlobalReceiverRegistry<H> receiver;
    protected final Set<Identifier> sendableChannels;

    protected int commonVersion = -1;

    protected AbstractChanneledNetworkAddon(GlobalReceiverRegistry<H> receiver, ClientConnection connection, String description) {
        super(receiver, description);
        this.connection = connection;
        this.receiver = receiver;
        this.sendableChannels = Collections.synchronizedSet(new HashSet<>());
    }

    protected void registerPendingChannels(ChannelInfoHolder holder, NetworkPhase state) {
        final Collection<Identifier> pending = holder.kessokulib$getPendingChannelsNames(state);

        if (!pending.isEmpty()) {
            register(new ArrayList<>(pending));
            pending.clear();
        }
    }

    // always supposed to handle async!
    public boolean handle(CustomPayload payload) {
        final Identifier channelName = payload.getId().id();
        this.logger.debug("Handling inbound packet from channel with name \"{}\"", channelName);

        // Handle reserved packets
        if (payload instanceof RegistrationPayload registrationPayload) {
            if (NetworkingImpl.REGISTER_CHANNEL.equals(channelName)) {
                this.receiveRegistration(true, registrationPayload);
                return true;
            }

            if (NetworkingImpl.UNREGISTER_CHANNEL.equals(channelName)) {
                this.receiveRegistration(false, registrationPayload);
                return true;
            }
        }

        @Nullable H handler = this.getHandler(channelName);

        if (handler == null) {
            return false;
        }

        try {
            this.receive(handler, payload);
        } catch (Throwable ex) {
            this.logger.error("Encountered exception while handling in channel with name \"{}\"", channelName, ex);
            throw ex;
        }

        return true;
    }

    protected abstract void receive(H handler, CustomPayload payload);

    protected void sendInitialChannelRegistrationPacket() {
        final RegistrationPayload payload = createRegistrationPayload(RegistrationPayload.REGISTER, this.getReceivableChannels());

        if (payload != null) {
            this.sendPacket(payload);
        }
    }

    @Nullable
    protected RegistrationPayload createRegistrationPayload(CustomPayload.Id<RegistrationPayload> id, Collection<Identifier> channels) {
        if (channels.isEmpty()) {
            return null;
        }

        return new RegistrationPayload(id, new ArrayList<>(channels));
    }

    // wrap in try with res (buf)
    protected void receiveRegistration(boolean register, RegistrationPayload payload) {
        if (register) {
            register(payload.channels());
        } else {
            unregister(payload.channels());
        }
    }

    void register(List<Identifier> ids) {
        ids.forEach(this::registerChannel);
        schedule(() -> this.invokeRegisterEvent(ids));
    }

    private void registerChannel(Identifier id) {
        if (this.sendableChannels.size() >= MAX_CHANNELS) {
            throw new IllegalArgumentException("Cannot register more than " + MAX_CHANNELS + " channels");
        }

        if (id.toString().length() > MAX_CHANNEL_NAME_LENGTH) {
            throw new IllegalArgumentException("Channel name is too long");
        }

        this.sendableChannels.add(id);
    }

    void unregister(List<Identifier> ids) {
        this.sendableChannels.removeAll(ids);
        schedule(() -> this.invokeUnregisterEvent(ids));
    }

    @Override
    public void sendPacket(Packet<?> packet, PacketCallbacks callback) {
        Objects.requireNonNull(packet, "Packet cannot be null");

        this.connection.send(packet, callback);
    }

    @Override
    public void disconnect(Text disconnectReason) {
        Objects.requireNonNull(disconnectReason, "Disconnect reason cannot be null");

        this.connection.disconnect(disconnectReason);
    }

    /**
     * Schedules a task to run on the main thread.
     */
    protected abstract void schedule(Runnable task);

    protected abstract void invokeRegisterEvent(List<Identifier> ids);

    protected abstract void invokeUnregisterEvent(List<Identifier> ids);

    public Set<Identifier> getSendableChannels() {
        return Collections.unmodifiableSet(this.sendableChannels);
    }

    // Common packet handlers

    @Override
    public void kessokulib$onCommonVersionPacket(int negotiatedVersion) {
        assert negotiatedVersion == 1; // We only support version 1 for now

        commonVersion = negotiatedVersion;
        this.logger.debug("Negotiated common packet version {}", commonVersion);
    }

    @Override
    public void kessokulib$onCommonRegisterPacket(CommonRegisterPayload payload) {
        if (payload.version() != kessokulib$getNegotiatedVersion()) {
            throw new IllegalStateException("Negotiated common packet version: %d but received packet with version: %d".formatted(commonVersion, payload.version()));
        }

        final String currentPhase = getPhase();

        if (currentPhase == null) {
            // We don't support receiving the register packet during this phase. See getPhase() for supported phases.
            // The normal case where the play channels are sent during configuration is handled in the client/common configuration packet handlers.
            logger.warn("Received common register packet for phase {} in network state: {}", payload.phase(), receiver.getPhase());
            return;
        }

        if (!payload.phase().equals(currentPhase)) {
            // We need to handle receiving the play phase during configuration!
            throw new IllegalStateException("Register packet received for phase (%s) on handler for phase(%s)".formatted(payload.phase(), currentPhase));
        }

        register(new ArrayList<>(payload.channels()));
    }

    @Override
    public CommonRegisterPayload kessokulib$createRegisterPayload() {
        return new CommonRegisterPayload(kessokulib$getNegotiatedVersion(), getPhase(), this.getReceivableChannels());
    }

    @Override
    public int kessokulib$getNegotiatedVersion() {
        if (commonVersion == -1) {
            throw new IllegalStateException("Not yet negotiated common packet version");
        }

        return commonVersion;
    }

    @Nullable
    private String getPhase() {
        return switch (receiver.getPhase()) {
            case PLAY -> CommonRegisterPayload.PLAY_PHASE;
            case CONFIGURATION -> CommonRegisterPayload.CONFIGURATION_PHASE;
            default -> null; // We don't support receiving this packet on any other phase
        };
    }
}
