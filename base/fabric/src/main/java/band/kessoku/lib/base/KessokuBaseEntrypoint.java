package band.kessoku.lib.base;

import net.fabricmc.api.ModInitializer;

public class KessokuBaseEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        ModUtils.getLogger().info(KessokuBase.MARKER, "KessokuLib-Base is loaded!");
    }
}
