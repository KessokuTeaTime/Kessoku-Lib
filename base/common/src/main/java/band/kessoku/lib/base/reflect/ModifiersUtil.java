package band.kessoku.lib.base.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class ModifiersUtil {

    /* === Common === */

    public static boolean isPublic(Field field) {
        return Modifier.isPublic(field.getModifiers());
    }

    public static boolean isPublic(Method method) {
        return Modifier.isPublic(method.getModifiers());
    }

    public static boolean isStatic(Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

    public static boolean isStatic(Method method) {
        return Modifier.isStatic(method.getModifiers());
    }

    /* === Common Combo === */

    public static boolean isPublicAndStatic(Field field) {
        return isPublic(field) && isStatic(field);
    }

    public static boolean isPublicAndStatic(Method method) {
        return isPublic(method) && isStatic(method);
    }

    public static boolean isPublicOrStatic(Field field, boolean shouldPublic, boolean shouldStatic) {
        return shouldPublic == isPublic(field) && shouldStatic == isStatic(field);
    }

    public static boolean isPublicOrStatic(Method method, boolean shouldPublic, boolean shouldStatic) {
        return shouldPublic == isPublic(method) && shouldStatic == isStatic(method);
    }

    /* === Common End === */

    /* === Data Object === */

    public static boolean isVolatile(Field field) {
        return Modifier.isVolatile(field.getModifiers());
    }

    public static boolean isVolatile(Method method) {
        return Modifier.isVolatile(method.getModifiers());
    }

    public static boolean isTransient(Field field) {
        return Modifier.isTransient(field.getModifiers());
    }

    public static boolean isTransient(Method method) {
        return Modifier.isTransient(method.getModifiers());
    }

    /* === Data Object End === */

    /* === Low Usage Tools === */

    public static boolean isFinal(Field field) {
        return Modifier.isFinal(field.getModifiers());
    }

    public static boolean isFinal(Method method) {
        return Modifier.isFinal(method.getModifiers());
    }

    public static boolean isProtected(Field field) {
        return Modifier.isProtected(field.getModifiers());
    }

    public static boolean isProtected(Method method) {
        return Modifier.isProtected(method.getModifiers());
    }

    /* === Low Usage Tools End === */
}
