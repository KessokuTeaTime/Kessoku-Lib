package band.kessoku.lib.events.lifecycle.mixin.neo;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.MinecraftServer;

import band.kessoku.lib.events.lifecycle.api.ServerLifecycleEvent;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Shadow
    private MinecraftServer.ResourceManagerHolder resourceManagerHolder;

    @Inject(method = "reloadResources", at = @At("HEAD"))
    private void startResourceReload(Collection<String> collection, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        ServerLifecycleEvent.START_DATA_PACK_RELOAD.invoker().startDataPackReload((MinecraftServer) (Object) this, this.resourceManagerHolder.resourceManager());
    }

    @Inject(method = "reloadResources", at = @At("TAIL"))
    private void endResourceReload(Collection<String> collection, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        cir.getReturnValue().handleAsync((value, throwable) -> {
            // Hook into fail
            ServerLifecycleEvent.END_DATA_PACK_RELOAD.invoker().endDataPackReload((MinecraftServer) (Object) this, this.resourceManagerHolder.resourceManager(), throwable == null);
            return value;
        }, (MinecraftServer) (Object) this);
    }

    @Inject(method = "save", at = @At("HEAD"))
    private void startSave(boolean suppressLogs, boolean flush, boolean force, CallbackInfoReturnable<Boolean> cir) {
        ServerLifecycleEvent.BEFORE_SAVE.invoker().onBeforeSaveData((MinecraftServer) (Object) this, flush, force);
    }

    @Inject(method = "save", at = @At("TAIL"))
    private void endSave(boolean suppressLogs, boolean flush, boolean force, CallbackInfoReturnable<Boolean> cir) {
        ServerLifecycleEvent.AFTER_SAVE.invoker().onAfterSaveData((MinecraftServer) (Object) this, flush, force);
    }
}
