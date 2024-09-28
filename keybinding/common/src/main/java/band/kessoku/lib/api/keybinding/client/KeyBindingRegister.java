package band.kessoku.lib.api.keybinding.client;

import band.kessoku.lib.impl.base.KessokuUtils;
import band.kessoku.lib.impl.keybinding.client.CategoryOrderMap;
import band.kessoku.lib.services.keybinding.client.KeyBindingRegisterService;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public final class KeyBindingRegister {
    private KeyBindingRegister() {
    }

    public static boolean addCategory(String categoryTranslationKey) {
        Map<String, Integer> map = CategoryOrderMap.getInstance();

        if (map.containsKey(categoryTranslationKey)) {
            return false;
        }

        final int largest = map.values().stream().max(Integer::compareTo).orElse(0);
        map.put(categoryTranslationKey, largest + 1);
        return true;
    }

    public boolean hasCategory(String categoryTranslationKey) {
        return CategoryOrderMap.getInstance().containsKey(categoryTranslationKey);
    }

    public boolean hasCategory(int index) {
        return !this.getCategorySet(index).isEmpty();
    }

    public Set<String> getCategorySet(int index) {
        return KessokuUtils.getKeysByValue(CategoryOrderMap.getInstance(), index);
    }

    public @Nullable Integer indexOf(String categoryTranslationKey) {
        return CategoryOrderMap.getInstance().get(categoryTranslationKey);
    }

    public boolean addCategory(int index, String categoryTranslationKey) {
        if (this.hasCategory(categoryTranslationKey)) return false;
        CategoryOrderMap.getInstance().putIfAbsent(categoryTranslationKey, index);
        return true;
    }

    public static KeyBinding register(KeyBinding keyBinding) {
        return KeyBindingRegisterService.INSTANCE.register(keyBinding);
    }

    public static InputUtil.Key getBoundKey(KeyBinding keyBinding) {
        return KeyBindingRegisterService.INSTANCE.getBoundKey(keyBinding);
    }
}
