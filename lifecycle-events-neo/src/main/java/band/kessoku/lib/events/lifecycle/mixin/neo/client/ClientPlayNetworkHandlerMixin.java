package band.kessoku.lib.events.lifecycle.mixin.neo.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.world.chunk.WorldChunk;

import band.kessoku.lib.events.lifecycle.api.client.ClientBlockEntityEvent;
import band.kessoku.lib.events.lifecycle.api.client.ClientEntityEvent;
import band.kessoku.lib.events.lifecycle.impl.LoadedChunksCache;

@Mixin(ClientPlayNetworkHandler.class)
abstract class ClientPlayNetworkHandlerMixin {
    @Shadow
    private ClientWorld world;

    @Inject(method = "onPlayerRespawn", at = @At(value = "NEW", target = "net/minecraft/client/world/ClientWorld"))
    private void onPlayerRespawn(PlayerRespawnS2CPacket packet, CallbackInfo ci) {
        // If a world already exists, we need to unload all (block)entities in the world.
        if (this.world != null) {
            for (Entity entity : this.world.getEntities()) {
                ClientEntityEvent.UNLOADED.invoker().onUnloaded(entity, this.world);
            }

            for (WorldChunk chunk : ((LoadedChunksCache) this.world).kessoku$getLoadedChunks()) {
                for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                    ClientBlockEntityEvent.UNLOADED.invoker().onUnloaded(blockEntity, this.world);
                }
            }
        }
    }

    /**
     * An explanation why we unload entities during onGameJoin:
     * Proxies such as Waterfall may send another Game Join packet if entity meta rewrite is disabled, so we will cover ourselves.
     * Velocity by default will send a Game Join packet when the player changes servers, which will create a new client world.
     * Also anyone can send another GameJoinPacket at any time, so we need to watch out.
     */
    @Inject(method = "onGameJoin", at = @At(value = "NEW", target = "net/minecraft/client/world/ClientWorld"))
    private void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        // If a world already exists, we need to unload all (block)entities in the world.
        if (this.world != null) {
            for (Entity entity : world.getEntities()) {
                ClientEntityEvent.UNLOADED.invoker().onUnloaded(entity, this.world);
            }

            for (WorldChunk chunk : ((LoadedChunksCache) this.world).kessoku$getLoadedChunks()) {
                for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                    ClientBlockEntityEvent.UNLOADED.invoker().onUnloaded(blockEntity, this.world);
                }
            }
        }
    }

    // Called when the client disconnects from a server or enters reconfiguration.
    @Inject(method = "clearWorld", at = @At("HEAD"))
    private void onClearWorld(CallbackInfo ci) {
        // If a world already exists, we need to unload all (block)entities in the world.
        if (this.world != null) {
            for (Entity entity : this.world.getEntities()) {
                ClientEntityEvent.UNLOADED.invoker().onUnloaded(entity, this.world);
            }

            for (WorldChunk chunk : ((LoadedChunksCache) this.world).kessoku$getLoadedChunks()) {
                for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                    ClientBlockEntityEvent.UNLOADED.invoker().onUnloaded(blockEntity, this.world);
                }
            }
        }
    }
}
