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
import band.kessoku.lib.impl.networking.client.ClientLoginNetworkAddon;
import band.kessoku.lib.impl.networking.payload.PacketByteBufLoginQueryRequestPayload;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.network.packet.s2c.login.LoginQueryRequestS2CPacket;

@Mixin(ClientLoginNetworkHandler.class)
abstract class ClientLoginNetworkHandlerMixin implements NetworkHandlerExtension {
    @Shadow
    @Final
    private MinecraftClient client;

    @Unique
    private ClientLoginNetworkAddon kessokulib$addon;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initAddon(CallbackInfo ci) {
        this.kessokulib$addon = new ClientLoginNetworkAddon((ClientLoginNetworkHandler) (Object) this, this.client);
        // A bit of a hack but it allows the field above to be set in case someone registers handlers during INIT event which refers to said field
        this.kessokulib$addon.lateInit();
    }

    @Inject(method = "onQueryRequest", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", remap = false, shift = At.Shift.AFTER), cancellable = true)
    private void handleQueryRequest(LoginQueryRequestS2CPacket packet, CallbackInfo ci) {
        if (packet.payload() instanceof PacketByteBufLoginQueryRequestPayload payload) {
            if (this.kessokulib$addon.handlePacket(packet)) {
                ci.cancel();
            } else {
                payload.data().skipBytes(payload.data().readableBytes());
            }
        }
    }

    @Override
    public ClientLoginNetworkAddon kessokulib$getNetworkAddon() {
        return this.kessokulib$addon;
    }
}
