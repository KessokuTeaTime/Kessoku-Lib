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
package band.kessoku.lib.data.example;

import band.kessoku.lib.impl.data.collection.Access;
import band.kessoku.lib.impl.data.structure.IntPropertyStructure;
import band.kessoku.lib.impl.data.structure.ProgressStructure;
import band.kessoku.lib.impl.data.structure.SidedInventoryStructure;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

// TODO consider moving to tests
public abstract class SimpleFunraceBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {
    public final SidedInventoryStructure items = integrate(new SidedInventoryStructure("Items",2));
    public final ProgressStructure progress = integrate(ProgressStructure.create("CookProgress", 200));

    public final IntPropertyStructure holder = integrate(IntPropertyStructure.of(
            progress.progressTime,
            progress.progressTimeTotal
    ));

    public final Access input = items.element("Input", 0).input(direction -> direction != Direction.DOWN);
    public final Access output = items.element("Output", 1);

    public SimpleFunraceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("container.furnace");
    }
}
