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

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public final class KessokuEntrypointFabric implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer, PreLaunchEntrypoint {
    @Override
    public void onInitialize() {
        ModUtils.getLogger().info(KessokuEntrypoint.MARKER, "KessokuLib-Entrypoint is loaded!");
    }

    @Override
    public void onInitializeClient() {
    }

    @Override
    public void onInitializeServer() {
    }

    @Override
    public void onPreLaunch() {
        KessokuEntrypoint.init();
    }
}
