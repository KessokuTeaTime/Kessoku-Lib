package band.kessoku.lib.command;

import band.kessoku.lib.command.events.CommandRegistryEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class KessokuCommandApiEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            CommandRegistryEvent.EVENT.invoker().register(dispatcher, registryAccess, environment);
        });
    }
}
