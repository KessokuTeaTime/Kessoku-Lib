package band.kessoku.lib.registry;

import band.kessoku.lib.base.ModUtils;

import net.fabricmc.api.ModInitializer;

public class KessokuRegistryEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        ModUtils.getLogger().info(KessokuRegistry.MARKER, "KessokuLib-Registry is loaded!");
    }
}
