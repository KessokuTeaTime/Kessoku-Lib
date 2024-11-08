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
package band.kessoku.lib.impl.event.lifecycle.fabric;

import band.kessoku.lib.api.KessokuLib;
import band.kessoku.lib.impl.event.lifecycle.KessokuLifecycleEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class KessokuLifecycleEventsFabric implements ModInitializer, ClientModInitializer {
    @Override
    public void onInitialize() {
        KessokuLib.loadModule(KessokuLifecycleEvents.class);
        KessokuLifecycleEventsImplFabric.registerCommonEvents();
    }

    @Override
    public void onInitializeClient() {
        KessokuLib.loadModule(KessokuLifecycleEvents.class);
        KessokuLifecycleEventsImplFabric.registerClientEvents();
    }
}
