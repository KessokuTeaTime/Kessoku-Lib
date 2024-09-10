package band.kessoku.lib.events.entity.mixin;

import java.util.Set;

import band.kessoku.lib.events.entity.api.ServerEntityWorldChangeEvent;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

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
