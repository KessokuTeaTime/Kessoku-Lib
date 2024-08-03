package band.kessoku.lib.platform.impl;

import band.kessoku.lib.platform.api.ModDependencyInfo;
import net.neoforged.neoforgespi.language.IModInfo;

public class ModDependencyInfoImpl implements ModDependencyInfo {
    private final IModInfo.ModVersion value;
    public ModDependencyInfoImpl(IModInfo.ModVersion modVersion) {
        value = modVersion;
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
