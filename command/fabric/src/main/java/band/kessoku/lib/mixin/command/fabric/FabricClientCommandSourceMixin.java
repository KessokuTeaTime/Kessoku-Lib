package band.kessoku.lib.mixin.command.fabric;

import band.kessoku.lib.api.util.command.ClientCommandSourceExtension;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FabricClientCommandSource.class)
public interface FabricClientCommandSourceMixin extends ClientCommandSourceExtension {
    @Override
    default void kessokulib$sendFeedback(Text message) {
        ((FabricClientCommandSource) this).sendFeedback(message);
    }

    @Override
    default void kessokulib$sendError(Text message) {
        ((FabricClientCommandSource) this).sendError(message);
    }

    @Override
    default MinecraftClient kessokulib$getClient() {
        return ((FabricClientCommandSource) this).getClient();
    }

    @Override
    default ClientPlayerEntity kessokulib$getPlayer() {
        return ((FabricClientCommandSource) this).getPlayer();
    }

    @Override
    default Entity kessokulib$getEntity() {
        return ((FabricClientCommandSource) this).getEntity();
    }

    @Override
    default Vec3d kessokulib$getPosition() {
        return ((FabricClientCommandSource) this).getPosition();
    }

    @Override
    default Vec2f kessokulib$getRotation() {
        return ((FabricClientCommandSource) this).getRotation();
    }

    @Override
    default ClientWorld kessokulib$getWorld() {
        return ((FabricClientCommandSource) this).getWorld();
    }
}
