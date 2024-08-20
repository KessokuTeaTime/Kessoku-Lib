package band.kessoku.lib.keybind;

import band.kessoku.lib.base.ModUtils;
import net.fabricmc.api.ClientModInitializer;

public class KessokuKeybindEntrypoint implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModUtils.getLogger().info(KessokuKeybind.MARKER, "KessokuKeybind is loaded!");
    }
}
