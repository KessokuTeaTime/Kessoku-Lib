package band.kessoku.gradle.plugin;

import net.fabricmc.loom.util.ModPlatform;

import java.util.Locale;

public enum PlatformIdentifier {
    FABRIC("Fabric", ModPlatform.FABRIC),
    NEO("Neo", ModPlatform.NEOFORGE)
    ;

    private final String displayName;
    private final ModPlatform modPlatform;

    PlatformIdentifier(String displayName, ModPlatform modPlatform) {
        this.displayName = displayName;
        this.modPlatform = modPlatform;
    }

    /**
     * Returns the lowercase ID of this mod platform.
     */
    public String id() {
        return name().toLowerCase(Locale.ROOT);
    }

    public String displayName() {
        return displayName;
    }

    public ModPlatform platform() {
        return modPlatform;
    }
}
