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
package band.kessoku.lib.impl.platform.neoforge;

import band.kessoku.lib.api.platform.DependencyInfo;
import net.neoforged.neoforgespi.language.IModInfo;

public final class DependencyInfoImpl implements DependencyInfo {
    private final IModInfo.ModVersion value;

    public DependencyInfoImpl(IModInfo.ModVersion modVersion) {
        this.value = modVersion;
    }

    @Override
    public DependencyKind getKind() {
        return switch (value.getType()) {
            case OPTIONAL -> DependencyKind.OPTIONAL;
            case REQUIRED -> DependencyKind.DEPENDS;
            case DISCOURAGED -> DependencyKind.CONFLICTS;
            case INCOMPATIBLE -> DependencyKind.BREAKS;
        };
    }

    @Override
    public String getModId() {
        return value.getModId();
    }

    public IModInfo.ModVersion getModVersion() {
        return value;
    }
}
