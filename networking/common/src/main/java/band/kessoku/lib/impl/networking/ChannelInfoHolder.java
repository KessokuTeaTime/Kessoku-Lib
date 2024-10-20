package band.kessoku.lib.impl.networking;

import java.util.Collection;

import net.minecraft.network.NetworkPhase;
import net.minecraft.util.Identifier;

public interface ChannelInfoHolder {
    /**
     * @return Channels which are declared as receivable by the other side but have not been declared yet.
     */
    Collection<Identifier> kessokulib$getPendingChannelsNames(NetworkPhase state);
}
