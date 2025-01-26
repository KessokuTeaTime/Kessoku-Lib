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
package band.kessoku.lib.api.platform;

import org.jetbrains.annotations.Nullable;

public interface DependencyInfo {
    /**
     * Get the kind of dependency.
     */
    DependencyKind getKind();

    /**
     * Returns the ID of the mod to check.
     */
    String getModId();

    enum DependencyKind {
        /**
         * It prevents the mod from loading if this dependency is missing.
         */
        DEPENDS("depends", "required", true, false),
        /**
         * When this dependency isn't present, the game will log a warning.
         * Only available in Fabric.
         */
        RECOMMENDS("recommends", null, true, true),
        /**
         * It's a kind of metadata.
         * Only available in Fabric.
         */
        SUGGESTS("suggests", null, true, true),
        /**
         * When this dependency is present, the game will log a warning.
         */
        CONFLICTS("conflicts", "discouraged", false, true),
        /**
         * It prevents the mod from loading if this dependency is present.
         */
        BREAKS("breaks", "incompatible", false, false),
        /**
         * It will not prevent the mod from loading if the dependency is missing, but still validates that the dependency is compatible.
         * Only available in NeoForge
         */
        OPTIONAL(null, "optional", true, false);
        @Nullable
        private final String fabricKey;
        @Nullable
        private final String neoKey;
        private final boolean positive;
        private final boolean soft;

        DependencyKind(@Nullable String fabricKey, @Nullable String neoKey, boolean positive, boolean soft) {
            this.fabricKey = fabricKey;
            this.neoKey = neoKey;
            this.positive = positive;
            this.soft = soft;
        }

        /**
         * Get whether the dependency is positive, encouraging the inclusion of a mod instead of negative/discouraging.
         */
        public boolean isPositive() {
            return positive;
        }

        /**
         * Get whether it is a soft dependency, allowing the mod to still load if the dependency is unmet.
         */
        public boolean isSoft() {
            return soft;
        }

        /**
         * Get the key for the dependency as used by fabric.mod.json (v1+) / neoforge.mods.toml and dependency overrides.
         */
        @Nullable
        public String getKey() {
            return Loader.isFabric() ? fabricKey : neoKey;
        }
    }
}
