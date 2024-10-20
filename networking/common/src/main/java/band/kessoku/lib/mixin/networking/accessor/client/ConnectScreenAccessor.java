package band.kessoku.lib.mixin.networking.accessor.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.network.ClientConnection;

@Mixin(ConnectScreen.class)
public interface ConnectScreenAccessor {
    @Accessor
    ClientConnection getConnection();
}
