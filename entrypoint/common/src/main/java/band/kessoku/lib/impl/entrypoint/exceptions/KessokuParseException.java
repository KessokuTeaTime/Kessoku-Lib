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
package band.kessoku.lib.impl.entrypoint.exceptions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class KessokuParseException extends RuntimeException {
    @Nullable
    public final String modid;

    public KessokuParseException(@NotNull String message, @Nullable String modid) {
        super("Failed to parse kessoku.json for " + modid + " : " + message);
        this.modid = modid;
    }

    public KessokuParseException(@NotNull String message, @Nullable String modid, @NotNull Throwable cause) {
        super("Failed to parse kessoku.json for " + modid + " : " + message, cause);
        this.modid = modid;
    }

    public KessokuParseException(@NotNull Throwable cause, @Nullable String modid) {
        super(cause);
        this.modid = modid;
    }
}
