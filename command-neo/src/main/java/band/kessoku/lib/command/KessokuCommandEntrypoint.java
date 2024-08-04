package band.kessoku.lib.command;

import band.kessoku.lib.command.impl.KessokuCommandImpl;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(KessokuCommand.MOD_ID)
public class KessokuCommandEntrypoint {
    public KessokuCommandEntrypoint(IEventBus modEventBus) {
        var forgeEventBus = NeoForge.EVENT_BUS;

        KessokuCommandImpl.registerCommonEvents(forgeEventBus);
    }
}
