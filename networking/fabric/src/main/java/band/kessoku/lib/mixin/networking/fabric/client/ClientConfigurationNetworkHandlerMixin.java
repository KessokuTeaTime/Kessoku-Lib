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
import band.kessoku.lib.impl.networking.client.ClientNetworkingImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConfigurationNetworkHandler;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.config.ReadyS2CPacket;

// We want to apply a bit earlier than other mods which may not use us in order to prevent refCount issues
@Mixin(value = ClientConfigurationNetworkHandler.class, priority = 999)
public abstract class ClientConfigurationNetworkHandlerMixin extends ClientCommonNetworkHandler implements NetworkHandlerExtension {
    @Unique
    private ClientConfigurationNetworkAddon kessokulib$addon;

    protected ClientConfigurationNetworkHandlerMixin(MinecraftClient client, ClientConnection connection, ClientConnectionState connectionState) {
        super(client, connection, connectionState);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initAddon(CallbackInfo ci) {
        this.kessokulib$addon = new ClientConfigurationNetworkAddon((ClientConfigurationNetworkHandler) (Object) this, this.client);
        // A bit of a hack but it allows the field above to be set in case someone registers handlers during INIT event which refers to said field
        ClientNetworkingImpl.setClientConfigurationAddon(this.kessokulib$addon);
        this.kessokulib$addon.lateInit();
    }

    @Inject(method = "onReady", at = @At(value = "NEW", target = "(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/network/ClientConnection;Lnet/minecraft/client/network/ClientConnectionState;)Lnet/minecraft/client/network/ClientPlayNetworkHandler;"))
    public void handleComplete(ReadyS2CPacket packet, CallbackInfo ci) {
        this.kessokulib$addon.handleComplete();
    }

    @Override
    public ClientConfigurationNetworkAddon kessokulib$getNetworkAddon() {
        return kessokulib$addon;
    }
}
