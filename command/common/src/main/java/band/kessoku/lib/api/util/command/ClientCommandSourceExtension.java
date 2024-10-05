package band.kessoku.lib.api.util.command;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public interface ClientCommandSourceExtension extends CommandSource {
    /**
     * Sends a feedback message to the player.
     *
     * @param message the feedback message
     */
    void kessokulib$sendFeedback(Text message);

    /**
     * Sends an error message to the player.
     *
     * @param message the error message
     */
    void kessokulib$sendError(Text message);

    /**
     * Gets the client instance used to run the command.
     *
     * @return the client
     */
    MinecraftClient kessokulib$getClient();

    /**
     * Gets the player that used the command.
     *
     * @return the player
     */
    ClientPlayerEntity kessokulib$getPlayer();

    /**
     * Gets the entity that used the command.
     *
     * @return the entity
     */
    default Entity kessokulib$getEntity() {
        return kessokulib$getPlayer();
    }

    /**
     * Gets the position from where the command has been executed.
     *
     * @return the position
     */
    default Vec3d kessokulib$getPosition() {
        return kessokulib$getPlayer().getPos();
    }

    /**
     * Gets the rotation of the entity that used the command.
     *
     * @return the rotation
     */
    default Vec2f kessokulib$getRotation() {
        return kessokulib$getPlayer().getRotationClient();
    }

    /**
     * Gets the world where the player used the command.
     *
     * @return the world
     */
    ClientWorld kessokulib$getWorld();

    /**
     * Gets the meta property under {@code key} that was assigned to this source.
     *
     * <p>This method should return the same result for every call with the same {@code key}.
     *
     * @param key the meta key
     * @return the meta
     */
    default Object kessokulib$getMeta(String key) {
        return null;
    }
}
