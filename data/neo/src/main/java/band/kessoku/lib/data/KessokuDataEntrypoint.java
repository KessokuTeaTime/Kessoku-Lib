package band.kessoku.lib.data;

import band.kessoku.lib.base.ModUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(KessokuData.MOD_ID)
public class KessokuDataEntrypoint {
    public KessokuDataEntrypoint(IEventBus modEventBus) {
        ModUtils.getLogger().info(KessokuData.MARKER, "KessokuLib-BlockEntity is Loaded!");
    }
}
