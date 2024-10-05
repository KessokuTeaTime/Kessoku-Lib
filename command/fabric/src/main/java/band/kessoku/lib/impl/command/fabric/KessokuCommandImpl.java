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
package band.kessoku.lib.impl.command.fabric;

import band.kessoku.lib.api.event.command.ClientCommandRegistryEvent;
import band.kessoku.lib.api.event.command.CommandRegistryEvent;
import band.kessoku.lib.api.util.command.ClientCommandSourceExtension;
import com.mojang.brigadier.CommandDispatcher;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public final class KessokuCommandImpl {
    @SuppressWarnings("unchecked")
    public static void registerClientEvents() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            ClientCommandRegistryEvent.EVENT.invoker().register((CommandDispatcher<ClientCommandSourceExtension>)
                    (CommandDispatcher<?>) dispatcher, registryAccess);
        });
    }

    public static void registerCommonEvents() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            CommandRegistryEvent.EVENT.invoker().register(dispatcher, registryAccess, environment);
        });
    }
}
