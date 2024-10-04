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
package band.kessoku.lib.mixin.event.lifecycle.neoforge;

import java.util.function.BooleanSupplier;

import band.kessoku.lib.api.event.lifecycle.ServerTickEvent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.world.ServerWorld;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    // Make sure "insideBlockTick" is true before we call the start tick, so inject after it is set
    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/server/world/ServerWorld;inBlockTick:Z", opcode = Opcodes.PUTFIELD, ordinal = 0, shift = At.Shift.AFTER))
    private void startWorldTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        ServerTickEvent.START_WORLD_TICK.invoker().onStartTick((ServerWorld) (Object) this);
    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void endWorldTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        ServerTickEvent.END_WORLD_TICK.invoker().onEndTick((ServerWorld) (Object) this);
    }
}
