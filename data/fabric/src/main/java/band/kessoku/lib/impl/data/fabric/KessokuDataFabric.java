package band.kessoku.lib.impl.data.fabric;

import band.kessoku.lib.api.KessokuLib;
import band.kessoku.lib.impl.data.KessokuData;
import net.fabricmc.api.ModInitializer;

public final class KessokuDataFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        KessokuLib.loadModule(KessokuData.class);
    }
}
