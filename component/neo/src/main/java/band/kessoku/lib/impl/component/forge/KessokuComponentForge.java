package band.kessoku.lib.impl.component.forge;

import band.kessoku.lib.api.KessokuLib;
import band.kessoku.lib.api.component.KessokuComponent;
import net.neoforged.fml.common.Mod;

@Mod(KessokuComponent.MOD_ID)
public class KessokuComponentForge {
    public KessokuComponentForge() {
        KessokuLib.loadModule(KessokuComponent.class);
        KessokuComponent.init();
    }
}
