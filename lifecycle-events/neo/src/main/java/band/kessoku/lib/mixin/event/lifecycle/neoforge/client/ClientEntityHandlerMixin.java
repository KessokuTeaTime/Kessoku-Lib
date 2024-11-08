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

import band.kessoku.lib.api.event.lifecycle.client.ClientEntityEvent;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/client/world/ClientWorld$ClientEntityHandler")
public class ClientEntityHandlerMixin {
    // final synthetic Lnet/minecraft/client/world/ClientWorld; field_27735
    @SuppressWarnings("ShadowTarget")
    @Shadow
    @Final
    private ClientWorld field_27735;

    // Call our load event after vanilla has loaded the entity
    @Inject(method = "startTracking(Lnet/minecraft/entity/Entity;)V", at = @At("TAIL"))
    private void invokeLoadEntity(Entity entity, CallbackInfo ci) {
        ClientEntityEvent.LOADED.invoker().onLoaded(entity, this.field_27735);
    }

    // Call our unload event before vanilla does.
    @Inject(method = "stopTracking(Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"))
    private void invokeUnloadEntity(Entity entity, CallbackInfo ci) {
        ClientEntityEvent.UNLOADED.invoker().onUnloaded(entity, this.field_27735);
    }
}
