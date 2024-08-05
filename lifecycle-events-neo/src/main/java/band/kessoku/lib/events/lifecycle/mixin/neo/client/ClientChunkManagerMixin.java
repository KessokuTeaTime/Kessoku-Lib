package band.kessoku.lib.events.lifecycle.mixin.neo.client;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.ChunkData;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

import band.kessoku.lib.events.lifecycle.api.client.ClientChunkEvent;

@Mixin(ClientChunkManager.class)
public abstract class ClientChunkManagerMixin {
    @Final
    @Shadow
    private ClientWorld world;

    @Inject(method = "loadChunkFromPacket", at = @At(value = "NEW", target = "net/minecraft/world/chunk/WorldChunk", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onChunkUnload(int x, int z, PacketByteBuf buf, NbtCompound tag, Consumer<ChunkData.BlockEntityVisitor> consumer, CallbackInfoReturnable<WorldChunk> info, int index, WorldChunk worldChunk, ChunkPos chunkPos) {
        if (worldChunk != null) {
            ClientChunkEvent.UNLOADED.invoker().onChunkUnloaded(this.world, worldChunk);
        }
    }

    @Inject(
            method = "updateLoadDistance",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/client/world/ClientChunkManager$ClientChunkMap.isInRadius(II)Z"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void onUpdateLoadDistance(int loadDistance, CallbackInfo ci, int oldRadius, int newRadius, ClientChunkManager.ClientChunkMap clientChunkMap, int k, WorldChunk oldChunk, ChunkPos chunkPos) {
        if (!clientChunkMap.isInRadius(chunkPos.x, chunkPos.z)) {
            ClientChunkEvent.UNLOADED.invoker().onChunkUnloaded(this.world, oldChunk);
        }
    }
}
