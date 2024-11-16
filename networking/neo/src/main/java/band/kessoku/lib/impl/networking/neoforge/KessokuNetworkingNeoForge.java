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
package band.kessoku.lib.impl.networking.neoforge;

import band.kessoku.lib.api.KessokuLib;
import band.kessoku.lib.api.KessokuNetworking;
import band.kessoku.lib.api.base.neoforge.NeoEventUtils;
import band.kessoku.lib.impl.networking.NetworkingImpl;
import band.kessoku.lib.impl.networking.client.ClientNetworkingImpl;

import net.minecraft.SharedConstants;
import net.minecraft.server.command.DebugConfigCommand;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod(KessokuNetworking.MOD_ID)
public class KessokuNetworkingNeoForge {
    public KessokuNetworkingNeoForge(IEventBus modEventBus) {
        KessokuLib.loadModule(KessokuNetworking.class);

        CommonPacketsImplNeoForge.init();
        NetworkingImpl.init();

        if (FMLLoader.getDist().isClient()) {
            ClientNetworkingImpl.clientInit();
        }

        NeoEventUtils.registerEvent(NeoForge.EVENT_BUS, RegisterCommandsEvent.class, event -> {
            if (SharedConstants.isDevelopment) {
                // Command is registered when isDevelopment is set.
                return;
            }

            if (FMLLoader.isProduction()) {
                // Only register this command in a dev env
                return;
            }

            DebugConfigCommand.register(event.getDispatcher());
        });

//        NeoEventUtils.registerEvent(modEventBus, RegisterConfigurationTasksEvent.class, event -> {
//            ServerConfigurationNetworkHandler listener = (ServerConfigurationNetworkHandler) event.getListener();
//            ServerConfigurationConnectionEvent.CONFIGURE.invoker().onSendConfiguration(listener, listener.server);
//        });
    }
}
