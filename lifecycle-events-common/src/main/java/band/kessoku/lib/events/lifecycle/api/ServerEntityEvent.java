package band.kessoku.lib.events.lifecycle.api;

import band.kessoku.lib.event.api.Event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;

public class ServerEntityEvent {
    /**
     * Called when an Entity is loaded into a ServerWorld.
     *
     * <p>When this event is called, the entity is already in the world.
     */
    public static final Event<Loaded> LOADED = Event.of(loadeds -> (entity, world) -> {
        for (Loaded loaded : loadeds) {
            loaded.onLoaded(entity, world);
        }
    });

    /**
     * Called when an Entity is unloaded from a ServerWorld.
     *
     * <p>This event is called before the entity is removed from the world.
     */
    public static final Event<Unloaded> UNLOADED = Event.of(unloadeds -> (entity, world) -> {
        for (Unloaded unloaded : unloadeds) {
            unloaded.onUnloaded(entity, world);
        }
    });

    /**
     * Called during {@link LivingEntity#tick()} if the Entity's equipment has been changed or mutated.
     *
     * <p>This event is also called when the entity joins the world.
     * A change in equipment is determined by {@link ItemStack#areEqual(ItemStack, ItemStack)}.
     */
    public static final Event<EquipmentChanged> EQUIPMENT_CHANGED = Event.of(equipmentChangeds -> (livingEntity, equipmentSlot, previous, next) -> {
        for (EquipmentChanged equipmentChanged : equipmentChangeds) {
            equipmentChanged.onChanged(livingEntity, equipmentSlot, previous, next);
        }
    });

    @FunctionalInterface
    public interface Loaded {
        void onLoaded(Entity entity, ServerWorld world);
    }

    @FunctionalInterface
    public interface Unloaded {
        void onUnloaded(Entity entity, ServerWorld world);
    }

    @FunctionalInterface
    public interface EquipmentChanged {
        void onChanged(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack previousStack, ItemStack currentStack);
    }
}
