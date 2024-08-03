package band.kessokuteatime.kessokulib.platform.api;

import org.jetbrains.annotations.Nullable;

public interface ModDependency {
    /**
     * Get the kind of dependency.
     */
    Kind getKind();
    /**
     * Returns the ID of the mod to check.
     */
    String getModId();

    enum Kind {
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

        Kind(@Nullable String fabricKey, @Nullable String neoKey, boolean positive, boolean soft) {
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
            return ModLoader.getInstance().isFabric() ? fabricKey : neoKey;
        }
    }
}
