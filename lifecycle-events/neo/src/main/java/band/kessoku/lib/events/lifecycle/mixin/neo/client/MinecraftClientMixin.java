package band.kessoku.lib.events.lifecycle.mixin.neo.client;

import band.kessoku.lib.events.lifecycle.api.client.ClientLifecycleEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Inject(at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;info(Ljava/lang/String;)V", shift = At.Shift.AFTER, remap = false), method = "stop")
    private void onStopping(CallbackInfo ci) {
        ClientLifecycleEvent.STOPPING.invoker().onClientStopping((MinecraftClient) (Object) this);
    }

    // We inject after the thread field is set so `ThreadExecutor#getThread` will work
    @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;thread:Ljava/lang/Thread;", shift = At.Shift.AFTER, ordinal = 0), method = "run")
    private void onStart(CallbackInfo ci) {
        ClientLifecycleEvent.STARTED.invoker().onClientStarted((MinecraftClient) (Object) this);
    }
}
