package band.kessoku.lib.keybind;

import band.kessoku.lib.base.ModUtils;
import band.kessoku.lib.keybind.impl.KeyBindRegisterImpl;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;

@Mod(value = KessokuKeybind.MOD_ID, dist = Dist.CLIENT)
public class KessokuKeyBindEntrypoint {
    public KessokuKeyBindEntrypoint(IEventBus modEventBus) {
        if (FMLLoader.getDist().isClient()) {
            ModUtils.getLogger().info(KessokuKeybind.MARKER, "KessokuKeybind is loaded!");
            KeyBindRegisterImpl.registerEvent(modEventBus);
        }
    }
}
