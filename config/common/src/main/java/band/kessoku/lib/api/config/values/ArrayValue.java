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
package band.kessoku.lib.api.config.values;

import java.util.Collection;

import band.kessoku.lib.api.config.Codec;
import band.kessoku.lib.api.config.ConfigValue;
import com.google.common.collect.Lists;

/**
 * These data structures have different syntax depending on the config type, <br>
 * so there is no default {@code codec}, and they usually need to be implemented manually through requirements <br>
 * (or by introducing a parser in the target format, like nightconfig's toml codec).
 *
 * @see Codec
 *
 * @author AmarokIce
 */
public class ArrayValue extends ConfigValue<Collection<ConfigValue<?>>> {
    public ArrayValue(Codec<Collection<ConfigValue<?>>> codec) {
        super(codec, Lists.newArrayList());
    }

    public ArrayValue(Codec<Collection<ConfigValue<?>>> codec, Collection<ConfigValue<?>> object) {
        super(codec, object);
    }
}
