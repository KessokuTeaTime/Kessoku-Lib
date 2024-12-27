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

import band.kessoku.lib.api.config.api.Codec;
import band.kessoku.lib.api.config.api.ConfigValue;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

/**
 * @author AmarokIce
 */
public class IdentifierValue extends ConfigValue<Identifier> {
    public static final Codec<Identifier> CODEC = new Codec<>() {
        @Override
        public Identifier encode(String valueStr) {
            return Identifier.of(valueStr);
        }

        @Override
        public String decode(Identifier value) {
            return value.toString();
        }
    };

    public IdentifierValue(String name) {
        super(CODEC, Identifier.of(name));
    }

    public IdentifierValue(Identifier object) {
        super(CODEC, object);
    }

    public IdentifierValue(Codec<Identifier> codec, String name) {
        super(codec, Identifier.of(name));
    }

    public IdentifierValue(Codec<Identifier> codec, Identifier object) {
        super(codec, object);
    }

    public Item asItem() {
        return Registries.ITEM.get(this.getValue());
    }

    public Block asBlock() {
        return Registries.BLOCK.get(this.getValue());
    }
}
