/*
 * Copyright (c) 2024, 2025 KessokuTeaTime
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

/**
 * This is an interface used to abstract a structure holding some data and implement processing logic.
 * <p>
 *     When building structure, data must be member variable and {@link DataStructure#integrate(Data) "integrate()"}
 *     can be used to hold data for other usages.
 * </p>
 */
public interface DataStructure {
    <T, K extends Data<T>> K integrate(K data);
    <K extends DataStructure> K integrate(K dataStructure);
}
