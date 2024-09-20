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
package band.kessoku.lib.registry.impl;

import band.kessoku.lib.registry.api.Registry;
import com.google.auto.service.AutoService;
import com.google.common.collect.Maps;
import net.minecraft.util.Identifier;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@AutoService(Registry.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public class RegistryImpl implements Registry {
    private static final Map<net.minecraft.registry.Registry, Map<Identifier, Object>> registries = new ConcurrentHashMap<>();
    private static boolean registered = false;

    @ApiStatus.Internal
    public static void onRegister(RegisterEvent event) {
        registries.forEach((registry, map) -> map.forEach((id, entry) -> event.register(registry.getKey(), id, () -> entry)));
        registered = true;
    }

    @Override
    public <V, T extends V> T registerInternal(net.minecraft.registry.Registry<V> registry, Identifier id, T entry) {
        if (registered) {
            throw new IllegalStateException("Cannot register new entries after net.neoforged.neoforge.registries.RegisterEvent has been fired.");
        }

        final var map = Objects.requireNonNull(registries.putIfAbsent(registry, Maps.newHashMap()));
        if (map.putIfAbsent(id, entry) != null) {
            throw new IllegalArgumentException("Duplicate registration " + id.toString());
        }

        return entry;
    }
}
