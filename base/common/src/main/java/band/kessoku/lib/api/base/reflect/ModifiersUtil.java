package band.kessoku.lib.api.base.reflect;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

public final class ModifiersUtil {
    private ModifiersUtil() {
    }

    /* === Common === */

    public static boolean isPublic(Member member) {
        return Modifier.isPublic(member.getModifiers());
    }

    public static boolean isStatic(Member member) {
        return Modifier.isStatic(member.getModifiers());
    }

    /* === Common Combo === */

    public static boolean isPublicAndStatic(Member member) {
        return isPublic(member) && isStatic(member);
    }

    public static boolean isPublicOrStatic(Member member, boolean shouldPublic, boolean shouldStatic) {
        return shouldPublic == isPublic(member) && shouldStatic == isStatic(member);
    }

    /* === Common End === */

    /* === Data Object === */

    public static boolean isVolatile(Member member) {
        return Modifier.isVolatile(member.getModifiers());
    }

    public static boolean isTransient(Member member) {
        return Modifier.isTransient(member.getModifiers());
    }

    /* === Data Object End === */

    /* === Low Usage Tools === */

    public static boolean isFinal(Member member) {
        return Modifier.isFinal(member.getModifiers());
    }

    public static boolean isProtected(Member member) {
        return Modifier.isProtected(member.getModifiers());
    }

    /* === Low Usage Tools End === */
}
