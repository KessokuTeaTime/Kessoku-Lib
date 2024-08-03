package band.kessoku.lib.event.events.command;

import band.kessoku.lib.event.api.Event;

public interface ClientCommandRegister {

    Event<ClientCommandRegister> EVENT = Event.of(clientCommandRegisters -> null);

    /**
     * Called when registering client commands.
     *
     * @param dispatcher the command dispatcher to register commands to
     * @param registryAccess object exposing access to the game's registries
     */
    // TODO
}
