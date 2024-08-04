package band.kessoku.lib.command.impl;

import band.kessoku.lib.command.api.events.CommandRegistryEvent;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class KessokuCommandImpl {
    public static void registerCommonEvents() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            CommandRegistryEvent.EVENT.invoker().register(dispatcher, registryAccess, environment);
        });
    }
}
