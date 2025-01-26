package band.kessoku.lib.impl.registry.fabric;

import band.kessoku.lib.api.registry.KessokuFuelRegistry;
import band.kessoku.lib.service.registry.FuelRegistryService;
import com.google.auto.service.AutoService;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.tag.TagKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AutoService(FuelRegistryService.class)
public class FuelRegistryImpl implements FuelRegistryService {
    public static final List<KessokuFuelRegistry.FuelTimeModifier> modifiers = new ArrayList<>();

    @Override
    public void registerFuel(ItemConvertible item, int time) {
        FuelRegistry.INSTANCE.add(item, time);
    }

    @Override
    public void registerFuel(TagKey<Item> tag, int time) {
        FuelRegistry.INSTANCE.add(tag, time);
    }

    @Override
    public void registerFuel(KessokuFuelRegistry.FuelTimeModifier modifier) {
        modifiers.add(modifier);
    }

    public static int test(ItemStack stack, RecipeType<?> recipeType) {
        final int baseTime = FuelRegistry.INSTANCE.get(stack.getItem());
        int time = baseTime;
        for (KessokuFuelRegistry.FuelTimeModifier modifier : modifiers) {
            Optional<Integer> optional = modifier.apply(stack, time, baseTime, recipeType);
            if (optional.isPresent()) time = optional.get();
        }
        return time;
    }
}
