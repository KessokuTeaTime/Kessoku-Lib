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
package band.kessoku.lib.impl.command.neoforge;

import band.kessoku.lib.api.base.neoforge.NeoEventUtils;
import band.kessoku.lib.api.event.command.ClientCommandRegistryEvent;
import band.kessoku.lib.api.event.command.CommandRegistryEvent;
import band.kessoku.lib.api.util.command.ClientCommandSourceExtension;
import com.mojang.brigadier.CommandDispatcher;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public final class KessokuCommandImpl {
    @SuppressWarnings("unchecked")
    public static void registerClientEvents(IEventBus forgeEventBus) {
        NeoEventUtils.registerEvent(forgeEventBus, RegisterClientCommandsEvent.class, event -> {
            ClientCommandRegistryEvent.EVENT.getInvoker().register((CommandDispatcher<ClientCommandSourceExtension>)
                    (CommandDispatcher<?>) event.getDispatcher(), event.getBuildContext());
        });
    }

    public static void registerCommonEvents(IEventBus forgeEventBus) {
        NeoEventUtils.registerEvent(forgeEventBus, RegisterCommandsEvent.class, event -> {
            CommandRegistryEvent.EVENT.getInvoker().register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
        });
    }
}
