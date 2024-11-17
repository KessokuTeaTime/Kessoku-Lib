package band.kessoku.lib.mixin.networking.neoforge;

import net.minecraft.network.NetworkPhase;
import net.minecraft.util.Identifier;
import net.neoforged.neoforge.network.registration.NetworkRegistry;
import net.neoforged.neoforge.network.registration.PayloadRegistration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(NetworkRegistry.class)
public interface NetworkRegistryAccessor {
    @Accessor("PAYLOAD_REGISTRATIONS")
    static Map<NetworkPhase, Map<Identifier, PayloadRegistration<?>>> getPayloadRegistrations() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static boolean getSetup() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static void setSetup(boolean setup) {
        throw new UnsupportedOperationException();
    }
}
