package band.kessoku.lib.events.entity;

import band.kessoku.lib.events.entity.api.EntityElytraEvent;
import band.kessoku.lib.events.entity.api.EntitySleepEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;

public class KessokuEntityEventEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        EntityElytraEvents.ALLOW.register(entity -> EntityElytraEvent.ALLOW.invoker().allowElytraFlight(entity));
        EntityElytraEvents.CUSTOM.register((entity, tickElytra) -> EntityElytraEvent.CUSTOM.invoker().useCustomElytra(entity, tickElytra));

        EntitySleepEvents.ALLOW_SLEEPING.register((player, sleepingPos) -> EntitySleepEvent.ALLOW_SLEEPING.invoker().allowSleep(player, sleepingPos));
        EntitySleepEvents.ALLOW_SLEEP_TIME.register((player, sleepingPos, vanillaResult) -> EntitySleepEvent.ALLOW_SLEEP_TIME.invoker().allowSleepTime(player, sleepingPos, vanillaResult));
    }
}
