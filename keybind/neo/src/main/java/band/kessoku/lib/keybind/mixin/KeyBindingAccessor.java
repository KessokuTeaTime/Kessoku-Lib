package band.kessoku.lib.keybind.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

@Mixin(KeyBinding.class)
public interface KeyBindingAccessor {
    @Accessor("CATEGORY_ORDER_MAP")
    static Map<String, Integer> kessoku$getCategoryMap() {
        throw new AssertionError();
    }

    @Accessor("boundKey")
    InputUtil.Key kessoku$getBoundKey();
}
