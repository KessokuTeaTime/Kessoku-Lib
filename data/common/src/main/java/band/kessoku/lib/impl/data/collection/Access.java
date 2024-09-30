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
package band.kessoku.lib.impl.data.collection;

import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public class Access extends Element<ItemStack> {
    public Predicate<Direction> input = direction -> true;
    public Predicate<Direction> output = direction -> true;
    public Predicate<ItemStack> itemstack = itemStack -> true;

    private Access(String id, int index, ListData<ItemStack> listData) {
        super(id, index, listData);
    }

    public static Access create(String id, int index, ListData<ItemStack> listData) {
        return new Access(id, index, listData);
    }

    public Access input(Predicate<Direction> condition) {
        input = input.and(condition);
        return this;
    }

    public Access output(Predicate<Direction> condition) {
        output = output.and(condition);
        return this;
    }

    public Access condition(Predicate<ItemStack> condition) {
        itemstack = itemstack.and(condition);
        return this;
    }
}
