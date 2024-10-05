package band.kessoku.lib.mixin.command;

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.server.command.HelpCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HelpCommand.class)
public interface HelpCommandAccessor {
    @Accessor("FAILED_EXCEPTION")
    static SimpleCommandExceptionType getFailedException() {
        throw new AssertionError("mixin");
    }
}
