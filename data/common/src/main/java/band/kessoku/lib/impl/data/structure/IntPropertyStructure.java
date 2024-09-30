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
package band.kessoku.lib.impl.data.structure;

import java.util.List;

import band.kessoku.lib.api.data.AbstractDataStructure;
import band.kessoku.lib.api.data.MutableData;

import net.minecraft.screen.PropertyDelegate;

@SuppressWarnings("unchecked")
public final class IntPropertyStructure extends AbstractDataStructure implements PropertyDelegate {

    private IntPropertyStructure(List<MutableData<Integer>> list) {
        list.forEach(this::integrate);
    }

    @SafeVarargs
    public static IntPropertyStructure of(MutableData<Integer>... data) {
        return new IntPropertyStructure(List.of(data));
    }

    @Override
    public int get(int index) {
        return ((MutableData<Integer>) dataList.get(index)).get();
    }

    @Override
    public void set(int index, int value) {
        ((MutableData<Integer>) dataList.get(index)).set(value);
    }

    @Override
    public int size() {
        return dataList.size();
    }
}
