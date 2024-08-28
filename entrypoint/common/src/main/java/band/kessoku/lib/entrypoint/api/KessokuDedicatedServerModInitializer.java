package band.kessoku.lib.entrypoint.api;

@FunctionalInterface
public interface KessokuDedicatedServerModInitializer {
    /**
     * Runs the mod initializer on the server environment.
     */
    void onInitializeServer();
}
