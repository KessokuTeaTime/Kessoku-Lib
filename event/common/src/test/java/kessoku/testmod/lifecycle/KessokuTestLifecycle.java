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
import band.kessoku.lib.api.event.ServerChunkEvents;
import band.kessoku.lib.api.event.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KessokuTestLifecycle implements KessokuModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("LifecycleEventsTest");

    @Override
    public void onInitialize() {
        //Server Chunk
        ServerChunkEvents.LOADED.register(((serverWorld, world) -> {
            //Do something
        }));
        ServerChunkEvents.UNLOADED.register(((serverWorld, world) -> {
            //Do something
        }));

        //Server Lifecycle
        ServerLifecycleEvents.STARTED.register(((minecraftServer) -> {
            LOGGER.info("Started Server!");
        }));
        ServerLifecycleEvents.STARTING.register(((minecraftServer) -> {
            LOGGER.info("Starting Server!");
        }));
        ServerLifecycleEvents.STOPPED.register(((minecraftServer) -> {
            LOGGER.info("Stopping Server!");
        }));
        ServerLifecycleEvents.STOPPING.register(((minecraftServer) -> {
            LOGGER.info("Stopped Server!");
        }));
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(((player, joined) -> {
            LOGGER.info("SyncDataPackContents received for {}", joined ? "join" : "reload");
        }));
        ServerLifecycleEvents.BEFORE_SAVE.register((server, flush, force) -> {
            LOGGER.info("Starting Save with settings: Flush:{} Force:{}", flush, force);
        });
        ServerLifecycleEvents.AFTER_SAVE.register((server, flush, force) -> {
            LOGGER.info("Save Finished with settings: Flush:{} Force:{}", flush, force);
        });
    }
}
