package band.kessokuteatime.kessokulib.event.events.command;

import band.kessokuteatime.kessokulib.event.api.Event;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.class_2168;
import net.minecraft.class_2170;
import net.minecraft.class_7157;

public interface CommandRegister {

    Event<CommandRegister> EVENT = Event.of();

    /**
     * Called when the server is registering commands.
     *
     * @param dispatcher the command dispatcher to register commands to
     * @param registryAccess object exposing access to the game's registries
     * @param environment environment the registrations should be done for, used for commands that are dedicated or integrated server only
     */
    void register(CommandDispatcher<class_2168> dispatcher, class_7157 registryAccess, class_2170.class_5364 environment);

}
