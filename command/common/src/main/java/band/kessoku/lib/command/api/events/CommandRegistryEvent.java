package band.kessoku.lib.command.api.events;

import band.kessoku.lib.event.api.Event;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public interface CommandRegistryEvent {

    Event<CommandRegistryEvent> EVENT = Event.of(commandRegistryEvents -> (dispatcher, registryAccess, environment) -> {
        for (CommandRegistryEvent commandRegistryEvent : commandRegistryEvents) {
            commandRegistryEvent.register(dispatcher, registryAccess, environment);
        }
    });

    /**
     * Called when the server is registering commands.
     *
     * @param dispatcher the command dispatcher to register commands to
     * @param registryAccess object exposing access to the game's registries
     * @param environment environment the registrations should be done for, used for commands that are dedicated or integrated server only
     */
    void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment);

}
