package band.kessoku.lib.mixins.keybinding.client;

import band.kessoku.lib.impl.keybinding.client.CategoryOrderMap;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

@Mixin(KeyBinding.class)
public class KeyBindingMixin {
    @SuppressWarnings("unchecked")
    @Redirect(method = "<clinit>", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/Util;make(Ljava/lang/Object;Ljava/util/function/Consumer;)Ljava/lang/Object;"))
    private static <T> T replaceCategoryOrderMap(T object, Consumer<? super T> initializer) {
        Map<String, Integer> map = (Map<String, Integer>) Util.make(object, initializer);
        return (T) Collections.synchronizedMap(CategoryOrderMap.of(map));
    }
}
