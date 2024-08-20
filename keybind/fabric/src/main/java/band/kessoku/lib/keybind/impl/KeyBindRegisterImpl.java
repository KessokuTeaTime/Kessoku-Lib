package band.kessoku.lib.keybind.impl;

import band.kessoku.lib.keybind.api.KeyBindRegister;
import com.google.auto.service.AutoService;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

@AutoService(KeyBindRegister.class)
public class KeyBindRegisterImpl implements KeyBindRegister {
    @Override
    public boolean addCategory(String categoryTranslationKey) {
        return KeyBindingRegistryImpl.addCategory(categoryTranslationKey);
    }

    @Override
    public KeyBinding register(KeyBinding keyBinding) {
        return KeyBindingHelper.registerKeyBinding(keyBinding);
    }

    @Override
    public InputUtil.Key getBoundKey(KeyBinding keyBinding) {
        return KeyBindingHelper.getBoundKeyOf(keyBinding);
    }
}
