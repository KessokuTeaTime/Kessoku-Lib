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
package band.kessoku.lib.mixin.event.entity;

import band.kessoku.lib.api.event.entity.ServerEntityWorldChangeEvent;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(Entity.class)
abstract class EntityMixin {
    @Shadow
    private World world;

    @Inject(method = "teleportTo", at = @At("RETURN"))
    private void afterWorldChanged(TeleportTarget target, CallbackInfoReturnable<Entity> cir) {
        // Ret will only have an entity if the teleport worked (entity not removed, teleportTarget was valid, entity was successfully created)
        Entity ret = cir.getReturnValue();

        if (ret != null) {
            ServerEntityWorldChangeEvent.AFTER_ENTITY_CHANGE_WORLD.invoker().afterChangeWorld((Entity) (Object) this, ret, (ServerWorld) this.world, (ServerWorld) ret.getWorld());
        }
    }

    /**
     * We need to fire the change world event for entities that are teleported using the `/teleport` command.
     */
    @Inject(method = "teleport(Lnet/minecraft/server/world/ServerWorld;DDDLjava/util/Set;FF)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setRemoved(Lnet/minecraft/entity/Entity$RemovalReason;)V"))
    private void afterEntityTeleportedToWorld(ServerWorld destination, double x, double y, double z, Set<PositionFlag> flags, float yaw, float pitch, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 1) Entity newEntity) {
        Entity originalEntity = (Entity) (Object) this;
        ServerEntityWorldChangeEvent.AFTER_ENTITY_CHANGE_WORLD.invoker().afterChangeWorld(originalEntity, newEntity, ((ServerWorld) originalEntity.getWorld()), destination);
    }
}
