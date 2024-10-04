/*
 * Copyright (c) 2024 KessokuTeaTime
 *
 * Licensed under the GNU Lesser General Pubic License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package band.kessoku.lib.mixin.event.lifecycle.neoforge.client;

import java.util.Map;

import band.kessoku.lib.api.event.lifecycle.ServerBlockEntityEvent;
import band.kessoku.lib.api.event.lifecycle.client.ClientBlockEntityEvent;
import com.llamalad7.mixinextras.sugar.Local;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

@Mixin(WorldChunk.class)
abstract class WorldChunkMixin {
    @Shadow
    public abstract World getWorld();

    @Inject(method = "setBlockEntity", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", shift = At.Shift.BY, by = 3))
    private void onLoadBlockEntity(BlockEntity blockEntity, CallbackInfo ci, @Local(ordinal = 1) BlockEntity removedBlockEntity) {
        // Only fire the load event if the block entity has actually changed
        if (blockEntity != null && blockEntity != removedBlockEntity) {
            if (this.getWorld() instanceof ServerWorld) {
                ServerBlockEntityEvent.LOADED.invoker().onLoaded(blockEntity, (ServerWorld) this.getWorld());
            } else if (this.getWorld() instanceof ClientWorld) {
                ClientBlockEntityEvent.LOADED.invoker().onLoaded(blockEntity, (ClientWorld) this.getWorld());
            }
        }
    }

    @Inject(method = "setBlockEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BlockEntity;markRemoved()V", shift = At.Shift.AFTER))
    private void onRemoveBlockEntity(BlockEntity blockEntity, CallbackInfo info, @Local(ordinal = 1) BlockEntity removedBlockEntity) {
        if (removedBlockEntity != null) {
            if (this.getWorld() instanceof ServerWorld) {
                ServerBlockEntityEvent.UNLOADED.invoker().onUnloaded(removedBlockEntity, (ServerWorld) this.getWorld());
            } else if (this.getWorld() instanceof ClientWorld) {
                ClientBlockEntityEvent.UNLOADED.invoker().onUnloaded(removedBlockEntity, (ClientWorld) this.getWorld());
            }
        }
    }

    // Use the slice to not redirect codepath where block entity is loaded
    @Redirect(
            method = "getBlockEntity(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/chunk/WorldChunk$CreationType;)Lnet/minecraft/block/entity/BlockEntity;",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;remove(Ljava/lang/Object;)Ljava/lang/Object;"
            ),
            slice = @Slice(
                    to = @At(
                            value = "FIELD",
                            target = "Lnet/minecraft/world/chunk/WorldChunk;blockEntityNbts:Ljava/util/Map;",
                            opcode = Opcodes.GETFIELD
                    )
            )
    )
    private <K, V> Object onRemoveBlockEntity(Map<K, V> map, K key) {
        @Nullable final V removed = map.remove(key);

        if (removed != null) {
            if (this.getWorld() instanceof ServerWorld) {
                ServerBlockEntityEvent.UNLOADED.invoker().onUnloaded((BlockEntity) removed, (ServerWorld) this.getWorld());
            } else if (this.getWorld() instanceof ClientWorld) {
                ClientBlockEntityEvent.UNLOADED.invoker().onUnloaded((BlockEntity) removed, (ClientWorld) this.getWorld());
            }
        }

        return removed;
    }

    @Inject(method = "removeBlockEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BlockEntity;markRemoved()V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void onRemoveBlockEntity(BlockPos pos, CallbackInfo ci, @Nullable BlockEntity removed) {
        if (removed != null) {
            if (this.getWorld() instanceof ServerWorld) {
                ServerBlockEntityEvent.UNLOADED.invoker().onUnloaded(removed, (ServerWorld) this.getWorld());
            } else if (this.getWorld() instanceof ClientWorld) {
                ClientBlockEntityEvent.UNLOADED.invoker().onUnloaded(removed, (ClientWorld) this.getWorld());
            }
        }
    }
}
