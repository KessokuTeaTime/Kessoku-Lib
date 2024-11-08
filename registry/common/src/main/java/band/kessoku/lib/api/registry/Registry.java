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
package band.kessoku.lib.api.registry;

import band.kessoku.lib.service.registry.RegistryService;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public final class Registry {
    private Registry() {
    }

    public static <T> T register(net.minecraft.registry.Registry<? super T> registry, String id, T entry) {
        return register(registry, Identifier.of(id), entry);
    }

    static <V, T extends V> T register(net.minecraft.registry.Registry<V> registry, Identifier id, T entry) {
        return RegistryService.getInstance().register(registry, id, entry);
    }

    static Item registerItem(Identifier id, Item.Settings settings) {
        return register(Registries.ITEM, id, new Item(settings));
    }

    static Item registerSimpleItem(Identifier id) {
        return register(Registries.ITEM, id, new Item(new Item.Settings()));
    }

    static Block registerBlock(Identifier id, AbstractBlock.Settings settings) {
        return register(Registries.BLOCK, id, new Block(settings));
    }

    static Block registerSimpleBlock(Identifier id) {
        return registerBlock(id, AbstractBlock.Settings.create());
    }

    static Item registerSimpleBlockItem(Identifier id, Block block) {
        return registerSimpleBlockItem(id, block, new Item.Settings());
    }

    static Item registerSimpleBlockItem(Identifier id, Block block, Item.Settings settings) {
        return register(Registries.ITEM, id, new BlockItem(block, settings));
    }

    static Item registerSimpleBlockItem(RegistryEntry<Block> block, Item.Settings settings) {
        return registerSimpleBlockItem(block.getKey().orElseThrow().getValue(), block.value(), settings);
    }

    static Item registerSimpleBlockItem(RegistryEntry<Block> block) {
        return registerSimpleBlockItem(block.getKey().orElseThrow().getValue(), block.value(), new Item.Settings());
    }
}
