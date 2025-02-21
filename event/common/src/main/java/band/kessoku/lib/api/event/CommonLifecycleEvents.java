/*
 * Copyright (c) 2024, 2025 KessokuTeaTime
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
package band.kessoku.lib.api.event;


import net.minecraft.registry.DynamicRegistryManager;

public class CommonLifecycleEvents {
    private CommonLifecycleEvents() {
    }

    /**
     * Called when tags are loaded or updated.
     */
    public static final Event<TagLoaded> TAG_LOADED = Event.of(callbacks -> (registries, client) -> {
        for (TagLoaded callback : callbacks) {
            callback.onTagsLoaded(registries, client);
        }
    });

    public interface TagLoaded {
        /**
         * @param registries Up-to-date registries from which the tags can be retrieved.
         * @param client     True if the client just received a sync packet, false if the server just (re)loaded the tags.
         */
        void onTagsLoaded(DynamicRegistryManager registries, boolean client);
    }
}
