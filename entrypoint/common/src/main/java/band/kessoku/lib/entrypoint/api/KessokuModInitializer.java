package band.kessoku.lib.entrypoint.api;

@FunctionalInterface
public interface KessokuModInitializer {
    /**
     * Runs the mod initializer.
     */
    void onInitialize();
}
