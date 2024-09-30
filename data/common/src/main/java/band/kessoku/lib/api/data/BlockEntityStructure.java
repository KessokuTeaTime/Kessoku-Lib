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

import band.kessoku.lib.mixins.data.BlockEntityMixin;

/**
 * Use this to realize interface injection for {@link net.minecraft.block.entity.BlockEntity BlockEntity}.
 * @see BlockEntityMixin
 */
public interface BlockEntityStructure extends DataStructure {
    /**
     * Use to integrate a {@link DataStructure DataStructure} to {@link BlockEntityStructure}.
     * @see BlockEntityMixin#integrate(DataStructure)
     */
    @Override
    default <K extends DataStructure> K integrate(K dataStructure) {
        return dataStructure;
    }

    /**
     * Use to integrate a {@link Data Data} to {@link BlockEntityStructure}.
     * @see BlockEntityMixin#integrate(Data)
     */
    @Override
    default <T, K extends Data<T>> K integrate(K data) {
        return data;
    }
}
