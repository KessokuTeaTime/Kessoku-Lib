/*
 * Copyright (c) 2024 KessokuTeaTime
 *
 * Licensed under the GNU Lesser General Pubic License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package band.kessoku.lib.mixin.keybinding.client;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

import band.kessoku.lib.impl.keybinding.client.CategoryOrderMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Util;

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
