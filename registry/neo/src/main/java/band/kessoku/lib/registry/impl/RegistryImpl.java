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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import band.kessoku.lib.registry.api.Registry;
import com.google.auto.service.AutoService;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.util.Identifier;

import net.neoforged.neoforge.registries.RegisterEvent;

@AutoService(Registry.class)
@SuppressWarnings("unchecked, rawtypes")
public class RegistryImpl implements Registry {
    private static final Map<net.minecraft.registry.Registry, Set<EntryWithId>> registries = new ConcurrentHashMap<>();
    private static boolean registered = false;

    @ApiStatus.Internal
    public static void onRegister(RegisterEvent event) {
        registries.forEach((registry, entryWithIds) ->
                entryWithIds.forEach(entryWithId ->
                        event.register(registry.getKey(), entryWithId.id(), () -> entryWithId.entry())
                )
        );
        registered = true;
    }

    @Override
    public <V, T extends V> T register(net.minecraft.registry.Registry<V> registry, Identifier id, T entry) {
        if (registered)
            throw new IllegalStateException("net.neoforged.neoforge.registries.RegisterEvent has already been called!");
        if (!registries.containsKey(registry)) registries.put(registry, new HashSet<>());
        registries.get(registry).add(new EntryWithId<>(id, entry));
        return entry;
    }

    record EntryWithId<T>(Identifier id, T entry) {
    }
}
