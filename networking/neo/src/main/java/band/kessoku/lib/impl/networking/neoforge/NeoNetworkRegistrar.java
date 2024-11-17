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

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import band.kessoku.lib.impl.networking.PayloadTypeRegistryImpl;
import band.kessoku.lib.mixin.networking.neoforge.NetworkRegistryAccessor;
import org.apache.commons.lang3.function.TriConsumer;
import org.jetbrains.annotations.Nullable;

import net.minecraft.network.NetworkPhase;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import net.neoforged.neoforge.common.extensions.ICommonPacketListener;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.ChannelAttributes;
import net.neoforged.neoforge.network.registration.NetworkPayloadSetup;
import net.neoforged.neoforge.network.registration.NetworkRegistry;

public class NeoNetworkRegistrar {
    // Not our actual codec, see NetworkRegistryMixin
    public static final PacketCodec<?, ?> DUMMY_CODEC = PacketCodec.ofStatic((a, b) -> {
        throw new UnsupportedOperationException();
    }, a -> {
        throw new UnsupportedOperationException();
    });

    private final NetworkPhase protocol;

    private final Map<Identifier, NeoPayloadHandler<?>> registeredPayloads = new HashMap<>();

    public NeoNetworkRegistrar(NetworkPhase protocol) {
        this.protocol = protocol;
    }

    public static boolean hasCodecFor(NetworkPhase protocol, NetworkSide flow, Identifier id) {
        PayloadTypeRegistryImpl<? extends PacketByteBuf> registry = getPayloadRegistry(protocol, flow);
        return registry.get(id) != null;
    }

    public static PayloadTypeRegistryImpl<? extends PacketByteBuf> getPayloadRegistry(NetworkPhase protocol, NetworkSide flow) {
        if (protocol == NetworkPhase.PLAY) {
            return flow == NetworkSide.SERVERBOUND ? PayloadTypeRegistryImpl.PLAY_C2S : PayloadTypeRegistryImpl.PLAY_S2C;
        } else if (protocol == NetworkPhase.CONFIGURATION) {
            return flow == NetworkSide.SERVERBOUND ? PayloadTypeRegistryImpl.CONFIG_C2S : PayloadTypeRegistryImpl.CONFIG_S2C;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public <P extends CustomPayload, C, H> boolean registerGlobalReceiver(CustomPayload.Id<P> type, NetworkSide packetFlow, H handler, Function<IPayloadContext, C> ctxFactory, TriConsumer<H, P, C> consumer) {
        NeoPayloadHandler<P> neoHandler = getOrRegisterNativeHandler(type);
        return neoHandler.registerGlobalHandler(packetFlow, handler, ctxFactory, consumer);
    }

    public <H> H unregisterGlobalReceiver(Identifier id, NetworkSide flow) {
        NeoPayloadHandler<?> neoHandler = registeredPayloads.get(id);
        return neoHandler != null ? neoHandler.unregisterGlobalHandler(flow) : null;
    }

    public Set<Identifier> getGlobalReceivers(NetworkSide flow) {
        return registeredPayloads.entrySet().stream()
            .filter(e -> e.getValue().hasGlobalHandler(flow))
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
    }

    public <P extends CustomPayload, C, H> boolean registerLocalReceiver(CustomPayload.Id<P> type, ICommonPacketListener listener, H handler, Function<IPayloadContext, C> ctxFactory, TriConsumer<H, P, C> consumer) {
        NeoPayloadHandler<P> neoHandler = getOrRegisterNativeHandler(type);
        return neoHandler.registerLocalReceiver(listener, handler, ctxFactory, consumer);
    }

    public <H> H unregisterLocalReceiver(Identifier id, ICommonPacketListener listener) {
        NeoPayloadHandler<?> neoHandler = registeredPayloads.get(id);
        return neoHandler != null ? neoHandler.unregisterLocalHandler(listener) : null;
    }

    public Set<Identifier> getLocalReceivers(ICommonPacketListener listener) {
        return registeredPayloads.entrySet().stream()
            .filter(e -> e.getValue().hasLocalHandler(listener))
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
    }

    public Set<Identifier> getLocalSendable(ICommonPacketListener listener) {
        NetworkPayloadSetup payloadSetup = ChannelAttributes.getPayloadSetup(listener.getConnection());
        if (payloadSetup == null) {
            return Set.of();
        }
        return payloadSetup.channels().get(this.protocol).keySet();
    }

    @SuppressWarnings("unchecked")
    private <P extends CustomPayload> NeoPayloadHandler<P> getOrRegisterNativeHandler(CustomPayload.Id<P> type) {
        return (NeoPayloadHandler<P>) registeredPayloads.computeIfAbsent(type.id(), k -> {
            NeoPayloadHandler<P> handler = new NeoPayloadHandler<>();
            boolean setup = NetworkRegistryAccessor.getSetup();

            NetworkRegistryAccessor.setSetup(false);
            NetworkRegistry.register(type, (PacketCodec<? super PacketByteBuf, P>) DUMMY_CODEC, handler, List.of(protocol), Optional.empty(), "1.0", true);
            NetworkRegistryAccessor.setSetup(setup);

            // TODO Send registration message when registering late
            return handler;
        });
    }

    public static class NeoPayloadHandler<P extends CustomPayload> implements IPayloadHandler<P> {
        private final Map<NetworkSide, NeoSubHandler<P, ?, ?>> globalReceivers = new HashMap<>();
        private final Map<ICommonPacketListener, NeoSubHandler<P, ?, ?>> localReceivers = new HashMap<>();

        @Override
        public void handle(P arg, IPayloadContext context) {
            NeoSubHandler globalHandler = globalReceivers.get(context.flow());
            if (globalHandler != null) {
                context.enqueueWork(() -> globalHandler.consumer().accept(globalHandler.handler(), arg, globalHandler.ctxFactory().apply(context)));
            }
            NeoSubHandler localHandler = localReceivers.get(context.listener());
            if (localHandler != null) {
                context.enqueueWork(() -> localHandler.consumer().accept(localHandler.handler(), arg, localHandler.ctxFactory().apply(context)));
            }
        }

        public boolean hasGlobalHandler(NetworkSide flow) {
            return globalReceivers.containsKey(flow);
        }

        public <C, H> boolean registerGlobalHandler(NetworkSide flow, H original, Function<IPayloadContext, C> ctxFactory, TriConsumer<H, P, C> consumer) {
            if (!hasGlobalHandler(flow)) {
                globalReceivers.put(flow, new NeoSubHandler<>(original, ctxFactory, consumer));
                return true;
            }
            return false;
        }

        public boolean hasLocalHandler(ICommonPacketListener listener) {
            return localReceivers.containsKey(listener);
        }

        public <C, H> boolean registerLocalReceiver(ICommonPacketListener listener, H original, Function<IPayloadContext, C> ctxFactory, TriConsumer<H, P, C> consumer) {
            if (!hasLocalHandler(listener)) {
                localReceivers.put(listener, new NeoSubHandler<>(original, ctxFactory, consumer));
                return true;
            }
            return false;
        }

        @Nullable
        public <H> H unregisterGlobalHandler(NetworkSide flow) {
            NeoSubHandler subHandler = globalReceivers.remove(flow);
            return subHandler != null ? (H) subHandler.handler() : null;
        }

        @Nullable
        public <H> H unregisterLocalHandler(ICommonPacketListener listener) {
            NeoSubHandler subHandler = localReceivers.remove(listener);
            return subHandler != null ? (H) subHandler.handler() : null;
        }
    }

    record NeoSubHandler<P extends CustomPayload, C, H>(H handler, Function<IPayloadContext, C> ctxFactory, TriConsumer<H, P, C> consumer) { }
}
