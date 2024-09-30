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
package band.kessoku.lib.api.data;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDataStructure implements DataStructure {
    protected final List<Data<?>> dataList = new ArrayList<>();
    protected final List<DataStructure> dataStructureList = new ArrayList<>();

    @Override
    public <T, K extends Data<T>> K integrate(K data) {
        dataList.add(data);
        return data;
    }

    @Override
    public <K extends DataStructure> K integrate(K dataStructure) {
        dataStructureList.add(dataStructure);
        return dataStructure;
    }
}
