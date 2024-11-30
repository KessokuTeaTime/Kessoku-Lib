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

import java.util.HashMap;
import java.util.Map;

import band.kessoku.lib.api.data.DataStructure;
import org.apache.commons.lang3.function.TriFunction;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Coordinator<S extends DataStructure> {
    private final Map<BlockEntityType<?>, TriFunction<BlockEntityType<?>, World, BlockPos, S>> map = new HashMap<>();

    public <T extends BlockEntity> void add(BlockEntityType<T> type, BlockEntityAccess<T, S> access) {
        map.put(type, ((t, world, blockPos) -> {
            if (t == type) {
                return access.to(type.get(world, blockPos));
            }
            return null;
        }));
    }

    public <T extends BlockEntity> S get(BlockEntityType<T> type, World world, BlockPos pos) {
        return map.get(type).apply(type, world, pos);
    }
}
