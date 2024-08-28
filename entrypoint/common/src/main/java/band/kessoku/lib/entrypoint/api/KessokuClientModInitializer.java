package band.kessoku.lib.entrypoint.api;

@FunctionalInterface
public interface KessokuClientModInitializer {
    /**
     * Runs the mod initializer on the client environment.
     */
    void onInitializeClient();
}
