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
package band.kessoku.lib.api.blockentity;

import band.kessoku.lib.api.data.Storage;
import band.kessoku.lib.impl.ItemSidinator;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import net.minecraft.block.entity.BlockEntityType;

public class KessokuBlockEntity {
    public static final String MOD_ID = "kessoku_blockentity";
    public static final String NAME = "Kessoku Block Entity API";
    public static final Marker MARKER = MarkerFactory.getMarker("[" + NAME +"]");

    public void init() {
        BlockEntityCoordinator.add(BlockEntityType.FURNACE, Storage::inventory);
        BlockEntityCoordinator.add(BlockEntityType.CHEST, Storage::inventory);
        BlockEntityCoordinator.add(
                BlockEntityType.FURNACE,
                (blockentity) -> ItemSidinator.builder().side(1).bottom(2, 1).top(0).build()
        );
    }
}
