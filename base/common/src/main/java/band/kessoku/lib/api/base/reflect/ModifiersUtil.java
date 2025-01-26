/*
 * Copyright (c) 2024, 2025 KessokuTeaTime
 *
 * Licensed under the GNU Lesser General Pubic License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
