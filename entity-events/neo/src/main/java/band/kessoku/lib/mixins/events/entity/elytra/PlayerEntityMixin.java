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
package band.kessoku.lib.mixins.events.entity.elytra;

import band.kessoku.lib.api.events.entity.EntityElytraEvent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(PlayerEntity.class)
abstract class PlayerEntityMixin extends LivingEntity {
    PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
        throw new AssertionError();
    }

    @Shadow
    public abstract void startFallFlying();

    /**
     * Allow the server-side and client-side elytra checks to fail when {@link EntityElytraEvent#ALLOW} blocks flight,
     * and otherwise to succeed for elytra flight through {@link EntityElytraEvent#CUSTOM}.
     */
    @SuppressWarnings("ConstantConditions")
    @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/entity/EquipmentSlot;CHEST:Lnet/minecraft/entity/EquipmentSlot;"), method = "checkFallFlying()Z", allow = 1, cancellable = true)
    void injectElytraCheck(CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity self = (PlayerEntity) (Object) this;

        if (!EntityElytraEvent.ALLOW.invoker().allowElytraFlight(self)) {
            cir.setReturnValue(false);
            return; // Return to prevent the rest of this injector from running.
        }

        if (EntityElytraEvent.CUSTOM.invoker().useCustomElytra(self, false)) {
            startFallFlying();
            cir.setReturnValue(true);
        }
    }
}
