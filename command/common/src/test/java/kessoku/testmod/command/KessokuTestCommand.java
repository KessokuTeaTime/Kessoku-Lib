package kessoku.testmod.command;

import band.kessoku.lib.command.api.events.CommandRegistryEvent;
import band.kessoku.lib.entrypoint.api.KessokuModInitializer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;

public class KessokuTestCommand implements KessokuModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistryEvent.EVENT.register(((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    CommandManager.literal("test")
                            .executes(context -> {
                                context.getSource().sendMessage(Text.literal("Hello world!"));
                                return 0;
                            })
            );
        }));
    }
}
