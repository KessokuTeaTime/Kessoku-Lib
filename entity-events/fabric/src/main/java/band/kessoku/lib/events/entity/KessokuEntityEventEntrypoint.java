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
package band.kessoku.lib.events.entity;

import band.kessoku.lib.events.entity.api.EntityElytraEvent;
import band.kessoku.lib.events.entity.api.EntitySleepEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;

public class KessokuEntityEventEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        EntityElytraEvents.ALLOW.register(entity -> EntityElytraEvent.ALLOW.invoker().allowElytraFlight(entity));
        EntityElytraEvents.CUSTOM.register((entity, tickElytra) -> EntityElytraEvent.CUSTOM.invoker().useCustomElytra(entity, tickElytra));

        EntitySleepEvents.ALLOW_SLEEPING.register((player, sleepingPos) -> EntitySleepEvent.ALLOW_SLEEPING.invoker().allowSleep(player, sleepingPos));
        EntitySleepEvents.ALLOW_SLEEP_TIME.register((player, sleepingPos, vanillaResult) -> EntitySleepEvent.ALLOW_SLEEP_TIME.invoker().allowSleepTime(player, sleepingPos, vanillaResult));
    }
}
