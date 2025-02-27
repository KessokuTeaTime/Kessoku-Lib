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
package band.kessoku.lib.impl.platform.fabric;

import band.kessoku.lib.api.platform.DependencyInfo;

import net.fabricmc.loader.api.metadata.ModDependency;

public final class DependencyInfoImpl implements DependencyInfo {
    private final ModDependency value;

    public DependencyInfoImpl(ModDependency dependency) {
        this.value = dependency;
    }

    @Override
    public DependencyKind getKind() {
        return switch (value.getKind()) {
            case DEPENDS -> DependencyKind.DEPENDS;
            case RECOMMENDS -> DependencyKind.RECOMMENDS;
            case SUGGESTS -> DependencyKind.SUGGESTS;
            case CONFLICTS -> DependencyKind.CONFLICTS;
            case BREAKS -> DependencyKind.BREAKS;
        };
    }

    @Override
    public String getModId() {
        return value.getModId();
    }

    public ModDependency getModDependency() {
        return value;
    }
}
