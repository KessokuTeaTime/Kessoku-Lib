package band.kessoku.lib.command;

import band.kessoku.lib.command.impl.KessokuCommandImpl;
import net.fabricmc.api.ModInitializer;

public class KessokuCommandEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        KessokuCommandImpl.registerCommonEvents();
    }
}
