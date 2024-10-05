package band.kessoku.lib.api.command.event;

import band.kessoku.lib.api.command.util.ClientCommandSourceExtension;
import band.kessoku.lib.event.api.Event;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandRegistryAccess;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface ClientCommandRegistryEvent {
    Event<ClientCommandRegistryEvent> EVENT = Event.of(clientCommandRegistryEvents -> (dispatcher, registryAccess) -> {
        for (ClientCommandRegistryEvent callback : clientCommandRegistryEvents) {
            callback.register(dispatcher, registryAccess);
        }
    });

    /**
     * Called when registering client commands.
     *
     * @param dispatcher the command dispatcher to register commands to
     * @param registryAccess object exposing access to the game's registries
     */
    void register(CommandDispatcher<ClientCommandSourceExtension> dispatcher, CommandRegistryAccess registryAccess);
}
