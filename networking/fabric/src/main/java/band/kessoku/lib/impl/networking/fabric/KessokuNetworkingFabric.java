package band.kessoku.lib.impl.networking.fabric;

import band.kessoku.lib.impl.networking.NetworkingImpl;
import band.kessoku.lib.impl.networking.client.ClientNetworkingImpl;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class KessokuNetworkingFabric implements ModInitializer, ClientModInitializer {
    @Override
    public void onInitialize() {
        CommonPacketsImplFabric.init();
        NetworkingImpl.init();
    }

    @Override
    public void onInitializeClient() {
        ClientNetworkingImpl.clientInit();
    }
}
