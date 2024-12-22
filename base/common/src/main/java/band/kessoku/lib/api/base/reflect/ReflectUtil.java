package band.kessoku.lib.api.base.reflect;

import band.kessoku.lib.api.KessokuLib;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Arrays;

public final class ReflectUtil {
    private ReflectUtil() {
    }

    public static boolean isAssignableFrom(Field field, Class<?>... clazzs) {
        var flag = Arrays.stream(clazzs).anyMatch(clazz -> !field.getType().isAssignableFrom(clazz));
        return !flag;
    }

    public static boolean markAccessible(AccessibleObject obj) {
        try {
            obj.setAccessible(true);
            return true;
        } catch (Exception e) {
            KessokuLib.getLogger().error(e.getMessage(), e);
            return false;
        }
    }
}
