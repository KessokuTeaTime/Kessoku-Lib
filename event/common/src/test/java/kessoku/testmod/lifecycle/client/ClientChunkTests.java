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
package kessoku.testmod.lifecycle.client;

import band.kessoku.lib.api.entrypoint.entrypoints.KessokuClientModInitializer;
import band.kessoku.lib.api.client.event.ClientChunkEvent;

public class ClientChunkTests implements KessokuClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientChunkEvent.LOADED.register((world, chunk) -> {
            //Do something in client
        });

        ClientChunkEvent.UNLOADED.register((world, chunk) -> {
            //Do something in client
        });
    }
}
