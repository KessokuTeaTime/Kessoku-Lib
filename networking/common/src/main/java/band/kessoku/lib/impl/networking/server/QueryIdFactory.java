package band.kessoku.lib.impl.networking.server;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tracks the current query id used for login query responses.
 */
interface QueryIdFactory {
    static QueryIdFactory create() {
        return new QueryIdFactory() {
            private final AtomicInteger currentId = new AtomicInteger();

            @Override
            public int nextId() {
                return this.currentId.getAndIncrement();
            }
        };
    }

    // called async prob.
    int nextId();
}
