package band.kessoku.lib.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;

public class ModUtils {
    public static <T> T loadService(Class<T> clazz) {
        return ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new AssertionError("No impl found for " + clazz.getName()));
    }

    public static Logger getLogger() {
        return LoggerFactory.getLogger("[KessokuLib]");
    }
}
