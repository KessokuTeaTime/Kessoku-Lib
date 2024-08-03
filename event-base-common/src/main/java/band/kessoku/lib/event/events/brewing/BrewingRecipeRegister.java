package band.kessoku.lib.event.events.brewing;

import band.kessoku.lib.event.api.Event;
import net.minecraft.recipe.BrewingRecipeRegistry;

public interface BrewingRecipeRegister {

    Event<BrewingRecipeRegister> EVENT = Event.of(brewingRecipeRegisters -> builder -> {
        for (BrewingRecipeRegister brewingRecipeRegister : brewingRecipeRegisters) {
            brewingRecipeRegister.build(builder);
        }
    });

    /**
     * @param builder brewing recipes builder.
     */
    void build(BrewingRecipeRegistry.Builder builder);
}
