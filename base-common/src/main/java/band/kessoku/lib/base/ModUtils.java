package band.kessoku.lib.base;

import java.util.ServiceLoader;

public class ModUtils {
    public static <T> T load(Class<T> clazz) {
        return ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new AssertionError("No impl found for " + clazz.getName()));
    }
}
