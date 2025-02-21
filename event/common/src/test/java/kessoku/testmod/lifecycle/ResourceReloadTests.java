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
package kessoku.testmod.lifecycle;

import band.kessoku.lib.api.entrypoint.entrypoints.KessokuModInitializer;
import band.kessoku.lib.api.event.ServerLifecycleEvents;

public class ResourceReloadTests implements KessokuModInitializer {
    @Override
    public void onInitialize() {
        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register(((server, serverResourceManager) -> {
            KessokuTestLifecycle.LOGGER.info("PREPARING FOR RELOAD");
        }));

        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(((server, serverResourceManager, success) -> {
            if (success) {
                KessokuTestLifecycle.LOGGER.info("FINISHED RELOAD on {}", Thread.currentThread());
            } else {
                // Failure can be tested by trying to disable the vanilla datapack
                KessokuTestLifecycle.LOGGER.error("FAILED TO RELOAD on {}", Thread.currentThread());
            }
        }));
    }
}
