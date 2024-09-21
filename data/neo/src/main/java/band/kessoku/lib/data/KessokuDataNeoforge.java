package band.kessoku.lib.data;

import band.kessoku.lib.base.ModUtils;
import net.neoforged.fml.common.Mod;

@Mod(KessokuData.MOD_ID)
public final class KessokuDataNeoforge {
    public KessokuDataNeoforge() {
        ModUtils.getLogger().info(KessokuData.MARKER, "KessokuLib-BlockEntity is Loaded!");
    }
}
