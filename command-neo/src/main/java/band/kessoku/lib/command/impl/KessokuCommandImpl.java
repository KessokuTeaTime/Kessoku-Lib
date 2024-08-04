package band.kessoku.lib.command.impl;

import band.kessoku.lib.command.api.events.CommandRegistryEvent;
import band.kessoku.lib.event.util.NeoEventUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class KessokuCommandImpl {
    public static void registerCommonEvents(IEventBus forgeEventBus) {
        NeoEventUtils.registerEvent(forgeEventBus, RegisterCommandsEvent.class, event -> {
            CommandRegistryEvent.EVENT.invoker().register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
        });
    }
}
