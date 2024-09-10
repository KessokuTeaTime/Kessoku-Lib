package band.kessoku.lib.events.entity.mixin;

import band.kessoku.lib.events.entity.api.ServerLivingEntityEvent;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;

@Mixin(MobEntity.class)
public class MobEntityMixin {
    @ModifyArg(method = "convertTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    private Entity afterEntityConverted(Entity converted, @Local(argsOnly = true) boolean keepEquipment) {
        ServerLivingEntityEvent.MOB_CONVERSION.invoker().onConversion((MobEntity) (Object) this, (MobEntity) converted, keepEquipment);
        return converted;
    }
}
