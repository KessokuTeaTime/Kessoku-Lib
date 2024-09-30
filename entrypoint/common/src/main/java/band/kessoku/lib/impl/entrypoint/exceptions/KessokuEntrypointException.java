/*
 * Copyright (c) 2024 KessokuTeaTime
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

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class KessokuEntrypointException extends RuntimeException {
    @Nullable
    public final String modid;
    public final String key;

    public KessokuEntrypointException(String key, Throwable cause) {
        super("Exception while loading entries for entrypoint '" + key + "'!", cause);
        this.modid = null;
        this.key = key;
    }

    public KessokuEntrypointException(String key, @NotNull String modid, Throwable cause) {
        super("Exception while loading entries for entrypoint '" + key + "' provided by '" + modid + "'", cause);
        this.modid = modid;
        this.key = key;
    }
}
