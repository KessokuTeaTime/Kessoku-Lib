package band.kessoku.lib.mixin.command.neoforge;

import band.kessoku.lib.api.util.command.ClientCommandSourceExtension;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

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
