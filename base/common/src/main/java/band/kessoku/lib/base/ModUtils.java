package band.kessoku.lib.base;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModUtils {
    public static <T> T loadService(Class<T> clazz) {
        return ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new AssertionError("No impl found for " + clazz.getName()));
    }

    public static Logger getLogger() {
        return LoggerFactory.getLogger("[KessokuLib]");
    }

    public static <T> boolean isType(List<?> list, Class<T> type) {
        for (Object element : list) {
            if (!(type.isInstance(element))) {
                return false;
            }
        }
        return true;
    }
    public static <V> V constructUnsafely(Class<V> cls) {
        try {
            Constructor<V> constructor = cls.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
