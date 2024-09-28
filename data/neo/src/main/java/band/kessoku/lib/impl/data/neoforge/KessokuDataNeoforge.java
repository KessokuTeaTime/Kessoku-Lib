package band.kessoku.lib.impl.data.neoforge;

import band.kessoku.lib.api.KessokuLib;
import band.kessoku.lib.impl.data.KessokuData;
import net.neoforged.fml.common.Mod;

@Mod(KessokuData.MOD_ID)
public final class KessokuDataNeoforge {
    public KessokuDataNeoforge() {
        KessokuLib.loadModule(KessokuData.class);
    }
}
