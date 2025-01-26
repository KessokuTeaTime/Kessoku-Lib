package band.kessoku.lib.impl.registry.neoforge;

import band.kessoku.lib.api.registry.KessokuFuelRegistry;
import band.kessoku.lib.service.registry.FuelRegistryService;
import com.google.auto.service.AutoService;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AutoService(FuelRegistryService.class)
public class FuelRegistryImpl implements FuelRegistryService {
    private static final Object2IntMap<Item> fuelMap = new Object2IntArrayMap<>();
    private static final List<KessokuFuelRegistry.FuelTimeModifier> modifiers = new ArrayList<>();

    @Override
    public void registerFuel(ItemConvertible item, int time) {
        fuelMap.put(item.asItem(), time);
    }

    @Override
    public void registerFuel(TagKey<Item> tag, int time) {
        for (RegistryEntry<Item> registryEntry : Registries.ITEM.iterateEntries(tag)) {
            fuelMap.put(registryEntry.value(), time);
        }
    }

    @Override
    public void registerFuel(KessokuFuelRegistry.FuelTimeModifier modifier) {
        modifiers.add(modifier);
    }

    @ApiStatus.Internal
    public static void onBurn(FurnaceFuelBurnTimeEvent event) {
        final ItemStack stack = event.getItemStack();
        final Item item = stack.getItem();
        final int baseTime = event.getBurnTime();
        final RecipeType<?> recipeType = event.getRecipeType();
        int time = baseTime;
        if (fuelMap.containsKey(item)) {
            time = fuelMap.getInt(item);
        }
        for (KessokuFuelRegistry.FuelTimeModifier modifier : modifiers) {
            Optional<Integer> optional = modifier.apply(stack, time, baseTime, recipeType);
            if (optional.isPresent()) time = optional.get();
        }
        if (time < 0) return;
        if (time != baseTime) event.setBurnTime(time);
    }
}
