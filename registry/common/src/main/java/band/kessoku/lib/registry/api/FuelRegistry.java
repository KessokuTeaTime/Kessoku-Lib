package band.kessoku.lib.registry.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;

/**
 * Poor compatibility in Fabric
 *
 * @param <T>
 */
@SuppressWarnings("unused, deprecation, rawtypes")
@ApiStatus.Experimental
public class FuelRegistry<T extends Recipe<?>> {
    private final Object2IntMap<ItemConvertible> itemRegistries = new Object2IntLinkedOpenHashMap<>();
    private final Object2IntMap<ItemWithData> dataRegistries = new Object2IntLinkedOpenHashMap<>();
    private final Object2IntMap<TagKey<Item>> tagRegistries = new Object2IntLinkedOpenHashMap<>();
    private static final Map<RecipeType, FuelRegistry> fuelRegistries = new HashMap<>();
    private final RecipeType<T> recipeType;

    private FuelRegistry(RecipeType<T> recipeType) {
        this.recipeType = recipeType;
    }

    /**
     * Get the FuelRegistry of the specific RecipeType
     *
     * @param recipeType The RecipeType
     */
    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> FuelRegistry<T> of(@NotNull RecipeType<T> recipeType) {
        return fuelRegistries.computeIfAbsent(recipeType, FuelRegistry::new);
    }

    public void add(ItemConvertible item, int cookTime) {
        this.itemRegistries.remove(item);
        this.itemRegistries.put(item, Math.max(0, cookTime));
    }

    public void add(TagKey<Item> tag, int cookTime) {
        this.tagRegistries.remove(tag);
        this.tagRegistries.put(tag, Math.max(0, cookTime));
    }

    public void add(ItemWithData data, int cookTime) {
        this.dataRegistries.remove(data);
        this.dataRegistries.put(data, Math.max(0, cookTime));
    }

    public void add(ItemStack stack, int cookTime) {
        this.add(ItemWithData.of(stack), cookTime);
    }

    /**
     * Get current modification in KessokuLib.
     */
    @Nullable
    public Integer get(ItemConvertible item) {
        if (this.itemRegistries.containsKey(item))
            return this.itemRegistries.get(item);
        final ArrayList<TagKey<Item>> tags = new ArrayList<>(this.tagRegistries.keySet());
        Collections.reverse(tags);
        RegistryEntry.Reference<Item> itemReference = item.asItem().getRegistryEntry();
        for (TagKey<Item> tag : tags) {
            if (itemReference.isIn(tag)) return this.tagRegistries.getInt(tag);
        }
        return null;
    }

    /**
     * Get current modification in KessokuLib.
     */
    @Nullable
    public Integer get(ItemWithData data) {
        if (this.dataRegistries.containsKey(data)) return this.dataRegistries.getInt(data);
        return this.get(data.item());
    }

    /**
     * Get current modification in KessokuLib.
     */
    @Nullable
    public Integer get(ItemStack stack) {
        return this.get(ItemWithData.of(stack));
    }

    @ApiStatus.Internal
    public static boolean canBurn(ItemStack stack) {
        return fuelRegistries.values().stream().map(registry -> registry.get(stack)).anyMatch(time -> time != null && time != 0);
    }

    public void remove(ItemConvertible item) {
        this.add(item, 0);
    }

    public void remove(TagKey<Item> tag) {
        this.add(tag, 0);
    }

    public void remove(ItemWithData data) {
        this.add(data, 0);
    }

    public void remove(ItemStack stack) {
        this.remove(ItemWithData.of(stack));
    }

    public void clear(ItemConvertible item) {
        this.itemRegistries.remove(item);
    }

    public void clear(TagKey<Item> tag) {
        this.tagRegistries.remove(tag);
    }

    public void clear(ItemWithData data) {
        this.dataRegistries.remove(data);
    }

    public void clear(ItemStack stack) {
        this.clear(ItemWithData.of(stack));
    }

    public RecipeType<T> getRecipeType() {
        return this.recipeType;
    }

    /**
     * Util method that set cooktime for SMELTING, BLASTING and SMOKING RecipeType.
     */
    public static void setFuelTime(ItemStack stack, int cookTime) {
        FuelRegistry.of(RecipeType.SMELTING).add(stack, cookTime);
        FuelRegistry.of(RecipeType.BLASTING).add(stack, cookTime);
        FuelRegistry.of(RecipeType.SMOKING).add(stack, cookTime);
    }

    /**
     * Util method that set cooktime for SMELTING, BLASTING and SMOKING RecipeType.
     */
    public static void setFuelTime(ItemConvertible item, int cookTime) {
        FuelRegistry.of(RecipeType.SMELTING).add(item, cookTime);
        FuelRegistry.of(RecipeType.BLASTING).add(item, cookTime);
        FuelRegistry.of(RecipeType.SMOKING).add(item, cookTime);
    }

    /**
     * @param block the block which acts like a furnace
     * @return The fuel time.
     */
    public static <T extends AbstractFurnaceBlock> int getFuelTime(T block, ItemStack stack) {
        BlockEntity entity = block.createBlockEntity(null, null);
        if (!(entity instanceof AbstractFurnaceBlockEntity)) return 0;
        return ((AbstractFurnaceBlockEntity) entity).getFuelTime(stack);
    }

    public record ItemWithData(ComponentMap componentMap, ItemConvertible item) {
        public static ItemWithData of(ItemStack stack) {
            return new ItemWithData(stack.getComponents(), stack.getItem());
        }
    }
}
