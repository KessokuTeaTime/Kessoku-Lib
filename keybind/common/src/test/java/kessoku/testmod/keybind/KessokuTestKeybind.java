package kessoku.testmod.keybind;

import band.kessoku.lib.entrypoint.api.KessokuModInitializer;
import band.kessoku.lib.keybind.api.KeyBindRegister;
import net.minecraft.client.option.KeyBinding;

public class KessokuTestKeybind implements KessokuModInitializer {
    @Override
    public void onInitialize() {
        KeyBindRegister.getInstance().register(
                new KeyBinding(
                "Test Name",
                0,
                "Test Category"
                )
        );
    }
}
