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
package band.kessoku.lib.impl.entrypoint.neoforge;

import band.kessoku.lib.api.entrypoint.KessokuEntrypoint;
import band.kessoku.lib.api.entrypoint.entrypoints.KessokuClientModInitializer;
import band.kessoku.lib.api.entrypoint.entrypoints.KessokuDedicatedServerModInitializer;
import band.kessoku.lib.api.entrypoint.entrypoints.KessokuModInitializer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(KessokuEntrypoint.MOD_ID)
public final class KessokuEntrypointNeoforge {
    public KessokuEntrypointNeoforge(Dist dist) {
        KessokuEntrypoint.invokeEntrypoint("main", KessokuModInitializer.class, KessokuModInitializer::onInitialize);
        if (dist.isClient())
            KessokuEntrypoint.invokeEntrypoint("client", KessokuClientModInitializer.class, KessokuClientModInitializer::onInitializeClient);
        if (dist.isDedicatedServer())
            KessokuEntrypoint.invokeEntrypoint("server", KessokuDedicatedServerModInitializer.class, KessokuDedicatedServerModInitializer::onInitializeServer);
    }
}
