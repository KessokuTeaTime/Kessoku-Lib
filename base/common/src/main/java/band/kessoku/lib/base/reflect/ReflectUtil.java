package band.kessoku.lib.base.reflect;

import org.apache.logging.log4j.core.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public final class ReflectUtil {
    public static void makeAccessible(Field field) {
        ReflectionUtil.makeAccessible(field);
    }

    public static void makeAccessible(Method method) {
        ReflectionUtil.makeAccessible(method);
    }

    public static boolean isAssignableFrom(Field field, Class<?> ... clazzs) {
        var flag = Arrays.stream(clazzs).anyMatch(clazz -> !field.getType().isAssignableFrom(clazz));
        return !flag;
    }
}
