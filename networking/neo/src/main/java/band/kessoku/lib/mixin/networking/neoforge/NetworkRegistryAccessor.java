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
package band.kessoku.lib.mixin.networking.neoforge;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.network.NetworkPhase;
import net.minecraft.util.Identifier;

import net.neoforged.neoforge.network.registration.NetworkRegistry;
import net.neoforged.neoforge.network.registration.PayloadRegistration;

@Mixin(NetworkRegistry.class)
public interface NetworkRegistryAccessor {
    @Accessor("PAYLOAD_REGISTRATIONS")
    static Map<NetworkPhase, Map<Identifier, PayloadRegistration<?>>> getPayloadRegistrations() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static boolean getSetup() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static void setSetup(boolean setup) {
        throw new UnsupportedOperationException();
    }
}
