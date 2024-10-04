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

import band.kessoku.lib.api.event.entity.ServerLivingEntityEvent;
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
