package band.kessoku.lib.api.registry;

import band.kessoku.lib.service.registry.FuelRegistryService;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class KessokuFuelRegistry {
    private KessokuFuelRegistry() {
    }

    public static void registerFuel(ItemConvertible item, int time) {
        FuelRegistryService.INSTANCE.registerFuel(item, time);
    }

    public static void registerFuel(TagKey<Item> tag, int time) {
        FuelRegistryService.INSTANCE.registerFuel(tag, time);
    }

    @ApiStatus.Experimental
    public static void registerFuel(FuelTimeModifier modifier) {
        FuelRegistryService.INSTANCE.registerFuel(modifier);
    }

    @FunctionalInterface
    @ApiStatus.Experimental
    public interface FuelTimeModifier {
        Optional<Integer> apply(ItemStack stack, Integer currentTime, Integer baseTime, @Nullable RecipeType<?> recipeType);
    }
}
