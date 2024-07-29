package band.kessokuteatime.kessokulib.event.base.fabric;

import net.fabricmc.api.ModInitializer;

import band.kessokuteatime.kessokulib.event.base.ExampleMod;

public final class ExampleModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ExampleMod.init();
    }
}
