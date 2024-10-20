package band.kessoku.lib.mixin.networking.accessor.client;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;

@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessor {
    @Nullable
    @Accessor("integratedServerConnection")
    ClientConnection getConnection();
}
