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
package band.kessoku.lib.mixin.command.neoforge;

import band.kessoku.lib.api.util.command.ClientCommandSourceExtension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Mixin(ClientCommandSource.class)
abstract class ClientCommandSourceMixin implements ClientCommandSourceExtension {
    @Shadow
    @Final
    private MinecraftClient client;

    @Override
    public void kessokulib$sendFeedback(Text message) {
        this.client.inGameHud.getChatHud().addMessage(message);
        this.client.getNarratorManager().narrate(message);
    }

    @Override
    public void kessokulib$sendError(Text message) {
        kessokulib$sendFeedback(Text.empty().append(message).formatted(Formatting.RED));
    }

    @Override
    public MinecraftClient kessokulib$getClient() {
        return client;
    }

    @Override
    public ClientPlayerEntity kessokulib$getPlayer() {
        return client.player;
    }

    @Override
    public ClientWorld kessokulib$getWorld() {
        return client.world;
    }
}
