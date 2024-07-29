package band.kessokuteatime.kessokulib.event.base.neoforge;

import net.neoforged.fml.common.Mod;

import band.kessokuteatime.kessokulib.event.base.ExampleMod;

@Mod(ExampleMod.MOD_ID)
public final class ExampleModNeoForge {
    public ExampleModNeoForge() {
        // Run our common setup.
        ExampleMod.init();
    }
}
