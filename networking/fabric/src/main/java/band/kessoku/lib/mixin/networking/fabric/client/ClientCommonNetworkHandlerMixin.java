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
package band.kessoku.lib.mixin.networking.fabric.client;

import band.kessoku.lib.impl.networking.NetworkHandlerExtension;
import band.kessoku.lib.impl.networking.client.ClientConfigurationNetworkAddon;
import band.kessoku.lib.impl.networking.client.ClientPlayNetworkAddon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;

@Mixin(ClientCommonNetworkHandler.class)
public abstract class ClientCommonNetworkHandlerMixin implements NetworkHandlerExtension {
    @Inject(method = "onCustomPayload(Lnet/minecraft/network/packet/s2c/common/CustomPayloadS2CPacket;)V", at = @At("HEAD"), cancellable = true)
    public void onCustomPayload(CustomPayloadS2CPacket packet, CallbackInfo ci) {
        final CustomPayload payload = packet.payload();
        boolean handled;

        if (this.kessokulib$getNetworkAddon() instanceof ClientPlayNetworkAddon addon) {
            handled = addon.handle(payload);
        } else if (this.kessokulib$getNetworkAddon() instanceof ClientConfigurationNetworkAddon addon) {
            handled = addon.handle(payload);
        } else {
            throw new IllegalStateException("Unknown network addon");
        }

        if (handled) {
            ci.cancel();
        }
    }
}
