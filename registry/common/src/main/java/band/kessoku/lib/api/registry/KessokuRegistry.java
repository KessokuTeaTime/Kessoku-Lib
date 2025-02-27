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
package band.kessoku.lib.api.registry;

import band.kessoku.lib.service.registry.RegistryService;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public final class KessokuRegistry {
    private KessokuRegistry() {
    }

    public static final String MOD_ID = "kessoku_registry";
    public static final String NAME = "Kessoku Registry API";
    public static final Marker MARKER = MarkerFactory.getMarker("[" + NAME + "]");

    public static <T> T register(Registry<? super T> registry, String id, T entry) {
        return register(registry, Identifier.of(id), entry);
    }

    public static <V, T extends V> T register(Registry<V> registry, Identifier id, T entry) {
        return RegistryService.INSTANCE.register(registry, id, entry);
    }

    public static Item registerItem(Identifier id, Item.Settings settings) {
        return register(Registries.ITEM, id, new Item(settings));
    }

    public static Item registerSimpleItem(Identifier id) {
        return register(Registries.ITEM, id, new Item(new Item.Settings()));
    }

    public static Block registerBlock(Identifier id, AbstractBlock.Settings settings) {
        return register(Registries.BLOCK, id, new Block(settings));
    }

    public static Block registerSimpleBlock(Identifier id) {
        return registerBlock(id, AbstractBlock.Settings.create());
    }

    public static Item registerSimpleBlockItem(Identifier id, Block block) {
        return registerSimpleBlockItem(id, block, new Item.Settings());
    }

    public static Item registerSimpleBlockItem(Identifier id, Block block, Item.Settings settings) {
        return register(Registries.ITEM, id, new BlockItem(block, settings));
    }

    public static Item registerSimpleBlockItem(RegistryEntry<Block> block, Item.Settings settings) {
        return registerSimpleBlockItem(block.getKey().orElseThrow().getValue(), block.value(), settings);
    }

    public static Item registerSimpleBlockItem(RegistryEntry<Block> block) {
        return registerSimpleBlockItem(block.getKey().orElseThrow().getValue(), block.value(), new Item.Settings());
    }
}
