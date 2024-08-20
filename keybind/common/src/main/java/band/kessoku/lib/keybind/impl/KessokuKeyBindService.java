package band.kessoku.lib.keybind.impl;

import band.kessoku.lib.base.ModUtils;
import band.kessoku.lib.keybind.api.KeyBindRegister;

public class KessokuKeyBindService {
    private static final KeyBindRegister register = ModUtils.loadService(KeyBindRegister.class);

    public static KeyBindRegister getRegister() {
        return register;
    }
}
