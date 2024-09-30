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
package kessoku.testmod.command;

import band.kessoku.lib.api.entrypoint.entrypoints.KessokuModInitializer;
import band.kessoku.lib.api.events.command.CommandRegistryEvent;

import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;

public class KessokuTestCommand implements KessokuModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistryEvent.EVENT.register(((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    CommandManager.literal("test")
                            .executes(context -> {
                                context.getSource().sendMessage(Text.literal("Hello world!"));
                                return 0;
                            })
            );
        }));
    }
}
