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
package band.kessoku.lib.api.component;

import band.kessoku.lib.api.registry.KessokuRegistry;
import band.kessoku.lib.impl.Progress;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import net.minecraft.component.ComponentType;
import net.minecraft.util.Identifier;

public class KessokuComponent {
    public static final String MOD_ID = "kessoku_component";
    public static final String NAME = "Kessoku Component API";
    public static final Marker MARKER = MarkerFactory.getMarker("[" + NAME +"]");

    public static final ComponentType<Progress> PROGRESS_COMPONENT_TYPE;

    public static void init() {

    }

    static {
        PROGRESS_COMPONENT_TYPE = KessokuRegistry.registerComponentType(
                Identifier.of("kessoku", "progress"),
                builder -> builder.codec(Progress.CODEC).packetCodec(Progress.PACKET_CODEC)
        );
    }
}
