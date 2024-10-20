package band.kessoku.lib.mixin.networking.fabric;

import com.mojang.brigadier.CommandDispatcher;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.SharedConstants;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.DebugConfigCommand;
import net.minecraft.server.command.ServerCommandSource;

import net.fabricmc.loader.api.FabricLoader;

@Mixin(CommandManager.class)
public class CommandManagerMixin {
    @Shadow
    @Final
    private CommandDispatcher<ServerCommandSource> dispatcher;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/dedicated/command/BanIpCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V"))
    private void init(CommandManager.RegistrationEnvironment environment, CommandRegistryAccess commandRegistryAccess, CallbackInfo ci) {
        if (SharedConstants.isDevelopment) {
            // Command is registered when isDevelopment is set.
            return;
        }

        if (!FabricLoader.getInstance().isDevelopmentEnvironment()) {
            // Only register this command in a dev env
            return;
        }

        DebugConfigCommand.register(this.dispatcher);
    }
}
