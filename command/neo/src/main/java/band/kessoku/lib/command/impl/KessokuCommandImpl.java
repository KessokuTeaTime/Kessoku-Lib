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
package band.kessoku.lib.command.impl;

import band.kessoku.lib.command.api.events.CommandRegistryEvent;
import band.kessoku.lib.event.api.util.neo.NeoEventUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public final class KessokuCommandImpl {
    public static void registerCommonEvents(IEventBus forgeEventBus) {
        NeoEventUtils.registerEvent(forgeEventBus, RegisterCommandsEvent.class, event -> {
            CommandRegistryEvent.EVENT.invoker().register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
        });
    }
}
