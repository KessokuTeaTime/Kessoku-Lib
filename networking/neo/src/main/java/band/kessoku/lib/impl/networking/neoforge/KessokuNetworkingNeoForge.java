package band.kessoku.lib.impl.networking.neoforge;

import band.kessoku.lib.api.KessokuLib;
import band.kessoku.lib.api.KessokuNetworking;
import band.kessoku.lib.api.base.neoforge.NeoEventUtils;
import band.kessoku.lib.api.networking.server.ServerConfigurationConnectionEvent;
import band.kessoku.lib.impl.networking.NetworkingImpl;
import band.kessoku.lib.impl.networking.client.ClientNetworkingImpl;
import band.kessoku.lib.impl.networking.common.CommonPacketsImpl;
import net.minecraft.SharedConstants;
import net.minecraft.server.command.DebugConfigCommand;
import net.minecraft.server.network.ServerConfigurationNetworkHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.event.RegisterConfigurationTasksEvent;

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
