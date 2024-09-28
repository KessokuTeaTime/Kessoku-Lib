package band.kessoku.lib.impl.keybinding.client;

import band.kessoku.lib.impl.base.KessokuUtils;
import net.minecraft.text.Text;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;

// RawDiamondMC Note:
// Maybe some hacky changes to implement insertAfter and insertBefore?
public final class CategoryOrderMap extends HashMap<String, Integer> {
    private static CategoryOrderMap INSTANCE;

    private CategoryOrderMap(Map<String, Integer> map) {
        super(map);
        INSTANCE = this;
    }

    @ApiStatus.Internal
    public static CategoryOrderMap of(Map<String, Integer> map) {
        if (INSTANCE != null)
            throw new UnsupportedOperationException("CATEGORY_ORDER_MAP has already been initialized!");
        return new CategoryOrderMap(map);
    }

    @Override
    public Integer put(String key, Integer value) {
        if (this.containsValue(value) && value != Integer.MAX_VALUE && value != Integer.MIN_VALUE)
            KessokuUtils.getLogger().warn(KessokuKeybinding.MARKER, "Duplicate category index found! Category: {} Index: {}", Text.translatable(key), value);
        return super.put(key, value);
    }

    public static CategoryOrderMap getInstance() {
        if (INSTANCE == null)
            throw new IllegalStateException("CATEGORY_ORDER_MAP has not been initialized yet!");
        return INSTANCE;
    }
}
