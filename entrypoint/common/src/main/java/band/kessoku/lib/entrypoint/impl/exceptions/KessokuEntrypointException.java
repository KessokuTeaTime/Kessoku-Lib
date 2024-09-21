package band.kessoku.lib.entrypoint.impl.exceptions;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class KessokuEntrypointException extends RuntimeException {
    @Nullable
    public final String modid;
    public final String key;

    public KessokuEntrypointException(String key, Throwable cause) {
        super("Exception while loading entries for entrypoint '" + key + "'!", cause);
        this.modid = null;
        this.key = key;
    }

    public KessokuEntrypointException(String key, @NotNull String modid, Throwable cause) {
        super("Exception while loading entries for entrypoint '" + key + "' provided by '" + modid + "'", cause);
        this.modid = modid;
        this.key = key;
    }
}
