package band.kessoku.lib.registry;

import band.kessoku.lib.registry.impl.RegistryImpl;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(KessokuRegistry.MOD_ID)
public class KessokuRegistryEntrypoint {
    public KessokuRegistryEntrypoint(IEventBus modEventBus) {
        new RegistryImpl().registerEvent(modEventBus);
    }
}
