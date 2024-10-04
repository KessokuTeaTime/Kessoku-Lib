package band.kessoku.lib.api.base.reflect;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

public final class ModifiersUtil {
    private ModifiersUtil() {
    }

    /* === Common === */

    public static boolean isPublic(Member field) {
        return Modifier.isPublic(field.getModifiers());
    }

    public static boolean isStatic(Member field) {
        return Modifier.isStatic(field.getModifiers());
    }

    /* === Common Combo === */

    public static boolean isPublicAndStatic(Member field) {
        return isPublic(field) && isStatic(field);
    }

    public static boolean isPublicOrStatic(Member field, boolean shouldPublic, boolean shouldStatic) {
        return shouldPublic == isPublic(field) && shouldStatic == isStatic(field);
    }

    /* === Common End === */

    /* === Data Object === */

    public static boolean isVolatile(Member field) {
        return Modifier.isVolatile(field.getModifiers());
    }

    public static boolean isTransient(Member field) {
        return Modifier.isTransient(field.getModifiers());
    }

    /* === Data Object End === */

    /* === Low Usage Tools === */

    public static boolean isFinal(Member field) {
        return Modifier.isFinal(field.getModifiers());
    }

    public static boolean isProtected(Member field) {
        return Modifier.isProtected(field.getModifiers());
    }

    /* === Low Usage Tools End === */
}
