package band.kessoku.lib.events.entity.api.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.event.GameEvent;

/**
 * An interface that can be implemented on an item to provide custom elytra flight when it is worn in the {@link EquipmentSlot#CHEST} slot.
 *
 * <p>To disable cape rendering when this item is worn (like the vanilla elytra item), have a look at {@code LivingEntityFeatureRenderEvents}.
 */
public interface KessokuElytraItem {
    /**
     * Try to use this custom elytra.
     *
     * @param entity     the entity
     * @param chestStack the stack currently worn in the chest slot, will always be of this item
     * @param tickElytra true to tick the elytra, false to only perform the check; vanilla-like elytras can use {@link #doVanillaElytraTick} to handle ticking
     * @return true to enable elytra flight for the entity
     */
    default boolean useCustomElytra(LivingEntity entity, ItemStack chestStack, boolean tickElytra) {
        if (ElytraItem.isUsable(chestStack)) {
            if (tickElytra) {
                doVanillaElytraTick(entity, chestStack);
            }

            return true;
        }

        return false;
    }

    /**
     * A helper to perform the default vanilla elytra tick logic: damage the elytra every 20 ticks, and send a game event every 10 ticks.
     */
    default void doVanillaElytraTick(LivingEntity entity, ItemStack chestStack) {
        int nextRoll = entity.getFallFlyingTicks() + 1;

        if (!entity.getWorld().isClient && nextRoll % 10 == 0) {
            if ((nextRoll / 10) % 2 == 0) {
                chestStack.damage(1, entity, EquipmentSlot.CHEST);
            }

            entity.emitGameEvent(GameEvent.ELYTRA_GLIDE);
        }
    }
}
