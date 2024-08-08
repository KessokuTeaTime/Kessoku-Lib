package band.kessoku.lib.registry.mixin;

import band.kessoku.lib.registry.api.FuelRegistry;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.math.BlockPos;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin {
    @Unique
    private RecipeType<? extends AbstractCookingRecipe> recipeType;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onCreate(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, RecipeType<? extends AbstractCookingRecipe> recipeType, CallbackInfo ci) {
        this.recipeType = recipeType;
    }

    @WrapMethod(method = "canUseAsFuel")
    private static boolean canUseAsFuelHook(ItemStack stack, Operation<Boolean> original) {
        return original.call(stack) || FuelRegistry.canBurn(stack);
    }

    @WrapMethod(method = "getFuelTime")
    private int getFuelTimeHook(ItemStack fuel, Operation<Integer> original) {
        if (fuel.isEmpty()) return 0;
        Integer fuelTime = FuelRegistry.of(this.recipeType).get(fuel);
        return fuelTime == null ? original.call(fuel) : fuelTime;
    }
}
