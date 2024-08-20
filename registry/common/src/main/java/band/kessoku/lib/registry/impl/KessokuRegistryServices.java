package band.kessoku.lib.registry.impl;

import band.kessoku.lib.base.ModUtils;
import band.kessoku.lib.registry.api.Registry;

public class KessokuRegistryServices {
    private static final Registry registry = ModUtils.loadService(Registry.class);

    public static Registry getRegistry() {
        return registry;
    }
}
