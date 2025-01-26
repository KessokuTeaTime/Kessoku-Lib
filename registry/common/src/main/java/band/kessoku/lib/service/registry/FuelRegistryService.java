package band.kessoku.lib.service.registry;

import band.kessoku.lib.api.KessokuLib;
import band.kessoku.lib.api.registry.KessokuFuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;

import java.util.function.BiFunction;

public interface FuelRegistryService {
    FuelRegistryService INSTANCE = KessokuLib.loadService(FuelRegistryService.class);

    void registerFuel(ItemConvertible item, int time);

    void registerFuel(TagKey<Item> tag, int time);

    void registerFuel(KessokuFuelRegistry.FuelTimeModifier modifier);
}
