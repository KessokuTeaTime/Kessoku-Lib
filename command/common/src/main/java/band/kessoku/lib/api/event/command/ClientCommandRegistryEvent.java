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
package band.kessoku.lib.api.event.command;

import band.kessoku.lib.api.event.Event;
import band.kessoku.lib.api.util.command.ClientCommandSourceExtension;
import com.mojang.brigadier.CommandDispatcher;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.command.CommandRegistryAccess;

@ApiStatus.NonExtendable
public interface ClientCommandRegistryEvent {
    Event<ClientCommandRegistryEvent> EVENT = Event.of(clientCommandRegistryEvents -> (dispatcher, registryAccess) -> {
        for (ClientCommandRegistryEvent callback : clientCommandRegistryEvents) {
            callback.register(dispatcher, registryAccess);
        }
    });

    /**
     * Called when registering client commands.
     *
     * @param dispatcher the command dispatcher to register commands to
     * @param registryAccess object exposing access to the game's registries
     */
    void register(CommandDispatcher<ClientCommandSourceExtension> dispatcher, CommandRegistryAccess registryAccess);
}
