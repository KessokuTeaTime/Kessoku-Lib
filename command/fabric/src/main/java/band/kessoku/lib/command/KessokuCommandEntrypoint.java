package band.kessoku.lib.command;

import band.kessoku.lib.base.ModUtils;
import band.kessoku.lib.command.impl.KessokuCommandImpl;

import net.fabricmc.api.ModInitializer;

public class KessokuCommandEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        ModUtils.getLogger().info(KessokuCommand.MARKER, "KessokuLib-Command is Loaded!");
        KessokuCommandImpl.registerCommonEvents();
    }
}
