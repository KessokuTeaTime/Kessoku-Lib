package band.kessokuteatime.kessokulib.event.events.brewing;

import band.kessokuteatime.kessokulib.event.api.Event;
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
