/*
 * Copyright (c) 2024, 2025 KessokuTeaTime
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
package band.kessoku.lib.mixin.event.entity;

import band.kessoku.lib.api.event.entity.EntitySleepEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.player.PlayerEntity;

@Mixin(PlayerEntity.class)
abstract class PlayerEntityMixin {
    @Inject(method = "canResetTimeBySleeping", at = @At("RETURN"), cancellable = true)
    private void onIsSleepingLongEnough(CallbackInfoReturnable<Boolean> info) {
        if (info.getReturnValueZ()) {
            info.setReturnValue(EntitySleepEvent.ALLOW_RESETTING_TIME.invoker().allowResettingTime((PlayerEntity) (Object) this));
        }
    }
}
