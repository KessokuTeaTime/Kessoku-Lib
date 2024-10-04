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
package band.kessoku.lib.impl.event.entity;

import band.kessoku.lib.api.KessokuLib;
import band.kessoku.lib.api.base.neoforge.NeoEventUtils;
import band.kessoku.lib.api.event.entity.EntitySleepEvent;
import band.kessoku.lib.api.event.entity.KessokuEntityEvents;
import band.kessoku.lib.api.event.entity.ServerLivingEntityEvent;
import band.kessoku.lib.api.event.entity.ServerPlayerEvent;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingConversionEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.CanContinueSleepingEvent;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@Mod(KessokuEntityEvents.MOD_ID)
public final class KessokuEntityEventsNeoforge {
    public KessokuEntityEventsNeoforge() {
        KessokuLib.loadModule(KessokuEntityEvents.class);
        NeoEventUtils.registerEvent(NeoForge.EVENT_BUS, LivingIncomingDamageEvent.class, event -> {
            var entity = event.getEntity();
            if (!entity.getWorld().isClient() && ServerLivingEntityEvent.ALLOW_DAMAGE.invoker().allowDamage(entity, event.getSource(), event.getAmount())) {
                event.setCanceled(true);
            }
        });

        NeoEventUtils.registerEvent(NeoForge.EVENT_BUS, CanPlayerSleepEvent.class, event -> {
            var failureReason = EntitySleepEvent.ALLOW_SLEEPING.invoker().allowSleep(event.getEntity(), event.getPos());

            if (failureReason != null) {
                event.setProblem(failureReason);
            }
        });

        NeoEventUtils.registerEvent(NeoForge.EVENT_BUS, CanContinueSleepingEvent.class, event -> {
            event.getEntity().getSleepingPosition().ifPresent(sleepingPos -> {
                if (event.getEntity() instanceof PlayerEntity player) {
                    var result = EntitySleepEvent.ALLOW_SLEEP_TIME.invoker().allowSleepTime(player, sleepingPos, !player.getWorld().isDay());
                    if (result != ActionResult.PASS) {
                        event.setContinueSleeping(result.isAccepted());
                    }
                }
            });
        });

        NeoEventUtils.registerEvent(NeoForge.EVENT_BUS, PlayerEvent.Clone.class, event -> {
            ServerPlayerEvent.COPY_FROM.invoker().copyFromPlayer((ServerPlayerEntity) event.getOriginal(), (ServerPlayerEntity) event.getEntity(), !event.isWasDeath());
        });

        NeoEventUtils.registerEvent(NeoForge.EVENT_BUS, LivingConversionEvent.class, event -> {
            if (event.getEntity() instanceof MobEntity mobEntity && event.getEntity() instanceof MobEntity converted) {
                ServerLivingEntityEvent.MOB_CONVERSION.invoker().onConversion(mobEntity, converted, false);
            }
        });
    }
}
