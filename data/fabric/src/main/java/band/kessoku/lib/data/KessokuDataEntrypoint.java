package band.kessoku.lib.data;

import band.kessoku.lib.base.ModUtils;
import net.fabricmc.api.ModInitializer;

public final class KessokuDataEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        ModUtils.getLogger().info(KessokuData.MARKER, "KessokuLib-BlockEntity is Loaded!");
    }
}
