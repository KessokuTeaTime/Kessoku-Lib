package band.kessoku.lib.mixin.networking.accessor.client;

import com.mojang.authlib.GameProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.network.ClientConfigurationNetworkHandler;

@Mixin(ClientConfigurationNetworkHandler.class)
public interface ClientConfigurationNetworkHandlerAccessor {
    @Accessor
    GameProfile getProfile();
}
