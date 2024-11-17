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
package band.kessoku.lib.impl.networking;

import band.kessoku.lib.api.networking.PayloadTypeRegistry;

import net.minecraft.util.Identifier;

public class NetworkingImpl {
    /**
     * Id of packet used to register supported channels.
     */
    public static final Identifier REGISTER_CHANNEL = Identifier.ofVanilla("register");

    /**
     * Id of packet used to unregister supported channels.
     */
    public static final Identifier UNREGISTER_CHANNEL = Identifier.ofVanilla("unregister");

    public static boolean isReservedCommonChannel(Identifier channelName) {
        return channelName.equals(REGISTER_CHANNEL) || channelName.equals(UNREGISTER_CHANNEL);
    }

    public static void init() {
        PayloadTypeRegistry.configS2C().register(RegistrationPayload.REGISTER, RegistrationPayload.REGISTER_CODEC);
        PayloadTypeRegistry.configS2C().register(RegistrationPayload.UNREGISTER, RegistrationPayload.UNREGISTER_CODEC);
        PayloadTypeRegistry.configC2S().register(RegistrationPayload.REGISTER, RegistrationPayload.REGISTER_CODEC);
        PayloadTypeRegistry.configC2S().register(RegistrationPayload.UNREGISTER, RegistrationPayload.UNREGISTER_CODEC);
        PayloadTypeRegistry.playS2C().register(RegistrationPayload.REGISTER, RegistrationPayload.REGISTER_CODEC);
        PayloadTypeRegistry.playS2C().register(RegistrationPayload.UNREGISTER, RegistrationPayload.UNREGISTER_CODEC);
        PayloadTypeRegistry.playC2S().register(RegistrationPayload.REGISTER, RegistrationPayload.REGISTER_CODEC);
        PayloadTypeRegistry.playC2S().register(RegistrationPayload.UNREGISTER, RegistrationPayload.UNREGISTER_CODEC);
    }
}
