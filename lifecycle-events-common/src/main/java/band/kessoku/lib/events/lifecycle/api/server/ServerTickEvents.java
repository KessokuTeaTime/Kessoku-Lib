package band.kessoku.lib.events.lifecycle.api.server;

import band.kessoku.lib.event.api.Event;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

public final class ServerTickEvents {

    /**
     * Called at the start of the server tick.
     */
    public static final Event<ServerTick.Start> START_SERVER_TICK = Event.of(starts -> server -> {
        for (ServerTick.Start start : starts) {
            start.onStartTick(server);
        }
    });

    /**
     * Called at the end of the server tick.
     */
    public static final Event<ServerTick.End> END_SERVER_TICK = Event.of(ends -> server -> {
        for (ServerTick.End end : ends) {
            end.onEndTick(server);
        }
    });

    /**
     * Called at the start of a ServerWorld's tick.
     */
    public static final Event<WorldTick.Start> START_WORLD_TICK = Event.of(starts -> world -> {
        for (WorldTick.Start start : starts) {
            start.onStartTick(world);
        }
    });

    /**
     * Called at the end of a ServerWorld's tick.
     *
     * <p>End of world tick may be used to start async computations for the next tick.
     */
    public static final Event<WorldTick.End> END_WORLD_TICK = Event.of(ends -> world -> {
        for (WorldTick.End callback : ends) {
            callback.onEndTick(world);
        }
    });

    public interface ServerTick {
        @FunctionalInterface
        interface Start {
            void onStartTick(MinecraftServer server);
        }

        @FunctionalInterface
        interface End {
            void onEndTick(MinecraftServer server);
        }
    }

    public interface WorldTick {
        @FunctionalInterface
        interface Start {
            void onStartTick(ServerWorld world);
        }

        @FunctionalInterface
        interface End {
            void onEndTick(ServerWorld world);
        }
    }
}
