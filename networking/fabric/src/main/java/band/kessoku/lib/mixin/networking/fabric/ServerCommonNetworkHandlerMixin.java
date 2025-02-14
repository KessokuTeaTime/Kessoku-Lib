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
package band.kessoku.lib.mixin.networking.fabric;

import band.kessoku.lib.impl.networking.NetworkHandlerExtension;
import band.kessoku.lib.impl.networking.server.ServerConfigurationNetworkAddon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.server.network.ServerCommonNetworkHandler;

@Mixin(ServerCommonNetworkHandler.class)
public abstract class ServerCommonNetworkHandlerMixin implements NetworkHandlerExtension {
    @Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
    private void handleCustomPayloadReceivedAsync(CustomPayloadC2SPacket packet, CallbackInfo ci) {
        final CustomPayload payload = packet.payload();

        boolean handled;

        if (kessokulib$getNetworkAddon() instanceof ServerConfigurationNetworkAddon addon) {
            handled = addon.handle(payload);
        } else {
            // Play should be handled in ServerPlayNetworkHandlerMixin
            throw new IllegalStateException("Unknown addon");
        }

        if (handled) {
            ci.cancel();
        }
    }

    @Inject(method = "onPong", at = @At("HEAD"))
    private void onPlayPong(CommonPongC2SPacket packet, CallbackInfo ci) {
        if (kessokulib$getNetworkAddon() instanceof ServerConfigurationNetworkAddon addon) {
            addon.onPong(packet.getParameter());
        }
    }
}
