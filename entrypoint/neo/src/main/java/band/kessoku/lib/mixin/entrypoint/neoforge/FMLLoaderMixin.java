/*
 * Copyright (c) 2024, 2025 KessokuTeaTime
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
package band.kessoku.lib.mixin.entrypoint.neoforge;

import band.kessoku.lib.api.KessokuLib;
import band.kessoku.lib.api.entrypoint.KessokuEntrypoint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.neoforged.fml.loading.FMLLoader;

@Mixin(FMLLoader.class)
public class FMLLoaderMixin {
    @Inject(method = "beforeStart", at = @At("HEAD"))
    private static void preLaunch(ModuleLayer gameLayer, CallbackInfo ci) {
        KessokuLib.loadModule(KessokuEntrypoint.class);
    }
}
