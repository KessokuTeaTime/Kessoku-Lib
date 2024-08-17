package band.kessoku.lib.config;

import band.kessoku.lib.config.impl.ModConfigHelperImpl;
import net.fabricmc.api.ModInitializer;

public class KessokuConfigEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        ModConfigHelperImpl.init();
    }
}
