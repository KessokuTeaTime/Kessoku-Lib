package band.kessoku.lib.base.reflect;

import java.lang.reflect.Field;
import java.util.Arrays;

public final class ReflectUtil {
    private ReflectUtil() {
    }

    public static boolean isAssignableFrom(Field field, Class<?>... clazzs) {
        var flag = Arrays.stream(clazzs).anyMatch(clazz -> !field.getType().isAssignableFrom(clazz));
        return !flag;
    }
}
