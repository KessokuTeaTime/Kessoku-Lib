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
package band.kessoku.lib.impl.networking.common;

import java.util.Arrays;
import java.util.function.Consumer;

import band.kessoku.lib.api.networking.server.ServerPlayNetworking;
import band.kessoku.lib.impl.networking.server.ServerConfigurationNetworkAddon;

import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayerConfigurationTask;

public class CommonPacketsImpl {
    public static final int PACKET_VERSION_1 = 1;
    public static final int[] SUPPORTED_COMMON_PACKET_VERSIONS = new int[]{ PACKET_VERSION_1 };

    // A configuration phase task to send and receive the version packets.
    public record CommonVersionConfigurationTask(ServerConfigurationNetworkAddon addon) implements ServerPlayerConfigurationTask {
        public static final Key KEY = new Key(CommonVersionPayload.ID.id().toString());

        @Override
        public void sendPacket(Consumer<Packet<?>> sender) {
            addon.sendPacket(new CommonVersionPayload(SUPPORTED_COMMON_PACKET_VERSIONS));
        }

        @Override
        public Key getKey() {
            return KEY;
        }
    }

    // A configuration phase task to send and receive the registration packets.
    public record CommonRegisterConfigurationTask(ServerConfigurationNetworkAddon addon) implements ServerPlayerConfigurationTask {
        public static final Key KEY = new Key(CommonRegisterPayload.ID.id().toString());

        @Override
        public void sendPacket(Consumer<Packet<?>> sender) {
            addon.sendPacket(new CommonRegisterPayload(addon.kessokulib$getNegotiatedVersion(), CommonRegisterPayload.PLAY_PHASE, ServerPlayNetworking.getGlobalReceivers()));
        }

        @Override
        public Key getKey() {
            return KEY;
        }
    }

    protected static int getNegotiatedVersion(CommonVersionPayload payload) {
        int version = getHighestCommonVersion(payload.versions(), SUPPORTED_COMMON_PACKET_VERSIONS);

        if (version <= 0) {
            throw new UnsupportedOperationException("server does not support any requested versions from client");
        }

        return version;
    }

    public static int getHighestCommonVersion(int[] a, int[] b) {
        int[] as = a.clone();
        int[] bs = b.clone();

        Arrays.sort(as);
        Arrays.sort(bs);

        int ap = as.length - 1;
        int bp = bs.length - 1;

        while (ap >= 0 && bp >= 0) {
            if (as[ap] == bs[bp]) {
                return as[ap];
            }

            if (as[ap] > bs[bp]) {
                ap--;
            } else {
                bp--;
            }
        }

        return -1;
    }
}
