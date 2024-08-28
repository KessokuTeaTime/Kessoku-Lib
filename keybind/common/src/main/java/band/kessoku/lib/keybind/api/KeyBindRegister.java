package band.kessoku.lib.keybind.api;

import band.kessoku.lib.keybind.impl.KessokuKeyBindService;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public interface KeyBindRegister {
    static KeyBindRegister getInstance() {
        return KessokuKeyBindService.getRegister();
    }

    boolean addCategory(String categoryTranslationKey);
    KeyBinding register(KeyBinding keyBinding);
    InputUtil.Key getBoundKey(KeyBinding keyBinding);
}
