package band.kessoku.lib.config;

import band.kessoku.lib.config.impl.ModConfigHelperImpl;
import net.neoforged.fml.common.Mod;

@Mod(KessokuConfig.MOD_ID)
public class KessokuConfigEntrypoint {
    public KessokuConfigEntrypoint() {
        ModConfigHelperImpl.init();
    }
}
