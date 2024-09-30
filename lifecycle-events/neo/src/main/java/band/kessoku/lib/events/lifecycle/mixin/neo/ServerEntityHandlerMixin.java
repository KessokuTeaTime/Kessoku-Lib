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
package band.kessoku.lib.events.lifecycle.mixin.neo;

import band.kessoku.lib.api.event.lifecycle.ServerEntityEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

@Mixin(targets = "net/minecraft/server/world/ServerWorld$ServerEntityHandler")
public class ServerEntityHandlerMixin {
    // final synthetic Lnet/minecraft/server/world/ServerWorld; field_26936
    @SuppressWarnings("ShadowTarget")
    @Shadow
    @Final
    private ServerWorld field_26936;

    @Inject(method = "startTracking(Lnet/minecraft/entity/Entity;)V", at = @At("TAIL"))
    private void invokeEntityLoadEvent(Entity entity, CallbackInfo ci) {
        ServerEntityEvent.LOADED.invoker().onLoaded(entity, this.field_26936);
    }

    @Inject(method = "stopTracking(Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"))
    private void invokeEntityUnloadEvent(Entity entity, CallbackInfo info) {
        ServerEntityEvent.UNLOADED.invoker().onUnloaded(entity, this.field_26936);
    }
}
