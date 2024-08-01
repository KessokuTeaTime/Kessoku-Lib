package band.kessokuteatime.kessokulib.event.events.brewing;

import band.kessokuteatime.kessokulib.event.api.Event;
import net.minecraft.class_1845;

public interface BrewingRecipeRegister {

    Event<BrewingRecipeRegister> EVENT = Event.of();

    /**
     * @author Oganesson897
     */
    void build(class_1845.class_9665 builder);
}
