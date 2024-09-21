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
package band.kessoku.lib.entrypoint;

import band.kessoku.lib.base.ModUtils;
import band.kessoku.lib.entrypoint.api.entrypoints.KessokuClientModInitializer;
import band.kessoku.lib.entrypoint.api.entrypoints.KessokuDedicatedServerModInitializer;
import band.kessoku.lib.entrypoint.api.entrypoints.KessokuModInitializer;
import com.google.common.collect.ImmutableList;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod(KessokuEntrypoint.MOD_ID)
public final class KessokuEntrypointEntrypoint {
    public KessokuEntrypointEntrypoint(Dist dist) {
    }

    public static void init() {
        KessokuEntrypoint.init();
    }
}
