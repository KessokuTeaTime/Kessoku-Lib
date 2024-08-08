package band.kessoku.lib.platform;

import band.kessoku.lib.base.ModUtils;
import net.neoforged.fml.common.Mod;

@Mod(KessokuPlatform.MOD_ID)
public class KessokuPlatformEntrypoint {
    public KessokuPlatformEntrypoint() {
        ModUtils.getLogger().info(KessokuPlatform.MARKER, "KessokuLib-Platform is loaded!");
    }
}
