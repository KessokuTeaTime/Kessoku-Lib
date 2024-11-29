package band.kessoku.lib.impl.component.fabric;

import band.kessoku.lib.api.KessokuLib;
import band.kessoku.lib.api.component.KessokuComponent;
import net.fabricmc.api.ModInitializer;

public class KessokuComponentFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        KessokuLib.loadModule(KessokuComponent.class);
        KessokuComponent.init();
    }
}
