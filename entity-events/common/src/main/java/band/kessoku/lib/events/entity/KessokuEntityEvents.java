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
import band.kessoku.lib.events.entity.api.item.KessokuElytraItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public final class KessokuEntityEvents {
    public static final String MOD_ID = "kessoku_entity_events";
    public static final Marker MARKER = MarkerFactory.getMarker("[KessokuEntityEvents]");
    static void init() {
        EntityElytraEvent.CUSTOM.register((entity, tickElytra) -> {
            ItemStack chestStack = entity.getEquippedStack(EquipmentSlot.CHEST);

            if (chestStack.getItem() instanceof KessokuElytraItem fabricElytraItem) {
                return fabricElytraItem.useCustomElytra(entity, chestStack, tickElytra);
            }

            return false;
        });
    }
}
