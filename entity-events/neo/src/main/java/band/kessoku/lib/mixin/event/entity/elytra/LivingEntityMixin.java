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
package band.kessoku.lib.mixin.event.entity.elytra;

import band.kessoku.lib.api.event.entity.EntityElytraEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

@SuppressWarnings("unused")
@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity {
    LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
        throw new AssertionError();
    }

    /**
     * Handle ALLOW and CUSTOM {@link EntityElytraEvent} when an entity is fall flying.
     */
    @SuppressWarnings("ConstantConditions")
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"), method = "tickFallFlying()V", allow = 1, cancellable = true)
    void injectElytraTick(CallbackInfo info) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (!EntityElytraEvent.ALLOW.invoker().allowElytraFlight(self)) {
            // The entity is already fall flying by now, we just need to stop it.
            if (!getWorld().isClient) {
                setFlag(Entity.FALL_FLYING_FLAG_INDEX, false);
            }

            info.cancel();
        }

        if (EntityElytraEvent.CUSTOM.invoker().useCustomElytra(self, true)) {
            // The entity is already fall flying by now, so all we need to do is an early return to bypass vanilla's own elytra check.
            info.cancel();
        }
    }
}
