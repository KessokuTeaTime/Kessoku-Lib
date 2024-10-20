package band.kessoku.lib.mixin.networking.accessor.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.network.ClientConnection;

@Mixin(ClientLoginNetworkHandler.class)
public interface ClientLoginNetworkHandlerAccessor {
    @Accessor
    ClientConnection getConnection();
}
