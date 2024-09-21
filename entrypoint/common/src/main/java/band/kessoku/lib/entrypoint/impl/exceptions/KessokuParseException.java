package band.kessoku.lib.entrypoint.impl.exceptions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class KessokuParseException extends RuntimeException {
    @Nullable
    public final String modid;

    public KessokuParseException(@NotNull String message, @Nullable String modid) {
        super("Failed to parse kessoku.json for " + modid + " : " + message);
        this.modid = modid;
    }

    public KessokuParseException(@NotNull String message, @Nullable String modid, @NotNull Throwable cause) {
        super("Failed to parse kessoku.json for " + modid + " : " + message, cause);
        this.modid = modid;
    }

    public KessokuParseException(@NotNull Throwable cause, @Nullable String modid) {
        super(cause);
        this.modid = modid;
    }
}
