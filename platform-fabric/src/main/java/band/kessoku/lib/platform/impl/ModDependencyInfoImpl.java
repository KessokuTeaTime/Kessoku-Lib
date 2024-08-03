package band.kessoku.lib.platform.impl;

import band.kessoku.lib.platform.api.ModDependencyInfo;
import net.fabricmc.loader.api.metadata.ModDependency;

public class ModDependencyInfoImpl implements ModDependencyInfo {
    private final ModDependency value;
    public ModDependencyInfoImpl(ModDependency dependency) {
        value = dependency;
    }
    @Override
    public DependencyKind getKind() {
        switch (value.getKind()) {
            case DEPENDS -> {
                return DependencyKind.DEPENDS;
            }
            case RECOMMENDS -> {
                return DependencyKind.RECOMMENDS;
            }
            case SUGGESTS -> {
                return DependencyKind.SUGGESTS;
            }
            case CONFLICTS -> {
                return DependencyKind.CONFLICTS;
            }
            case BREAKS -> {
                return DependencyKind.BREAKS;
            }
        }
        return null;
    }

    @Override
    public String getModId() {
        return value.getModId();
    }

    public ModDependency getModDependency() {
        return value;
    }
}
