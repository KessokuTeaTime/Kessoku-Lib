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
package band.kessoku.lib.platform.impl;

import band.kessoku.lib.platform.api.ModDependencyInfo;

import net.neoforged.neoforgespi.language.IModInfo;

public class ModDependencyInfoImpl implements ModDependencyInfo {
    private final IModInfo.ModVersion value;
    public ModDependencyInfoImpl(IModInfo.ModVersion modVersion) {
        this.value = modVersion;
    }
    @Override
    public DependencyKind getKind() {
        switch (value.getType()) {
            case OPTIONAL -> {
                return DependencyKind.OPTIONAL;
            }
            case REQUIRED -> {
                return DependencyKind.DEPENDS;
            }
            case DISCOURAGED -> {
                return DependencyKind.CONFLICTS;
            }
            case INCOMPATIBLE -> {
                return DependencyKind.BREAKS;
            }
        }
        return null;
    }

    @Override
    public String getModId() {
        return value.getModId();
    }

    public IModInfo.ModVersion getModVersion() {
        return value;
    }
}
