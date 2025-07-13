package band.kessoku.lib.impl;

import band.kessoku.lib.api.blockentity.Data;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Abstract base class for custom block entities with managed data fields.
 * Provides automatic NBT serialization for registered data fields and type-safe access utilities.
 * <p>
 * This implementation:
 * <ul>
 *   <li>Automatically handles NBT serialization/deserialization for all registered fields</li>
 *   <li>Maintains strict type association through registration</li>
 *   <li>Provides built-in support for primitive types, UUIDs and ItemStacks</li>
 *   <li>Enables type-safe data retrieval with error handling</li>
 * </ul>
 *
 * <h3>Usage Example:</h3>
 * <pre>
 * public class DemoBlockEntity extends KBlockEntity {
 *     private final Data<Integer> counter = intData("counter", 0);
 *     private final Data<ItemStack> item = itemStackData("item", ItemStack.EMPTY);
 *
 *     public DemoBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
 *         super(type, pos, state);
 *     }
 * }
 * </pre>
 */
public abstract class KBlockEntity extends BlockEntity {
    private final Map<String, Data<?>> dataMap = new TreeMap<>();

    public KBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        for (Data<?> data : dataMap.values()) {
            data.serialize(nbt, registries);
        }
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        for (Data<?> data : dataMap.values()) {
            data.deserialize(nbt, registries);
        }
    }

    /**
     * Registers a data field for automatic serialization and management.
     *
     * @param <V>   The concrete Data type
     * @param value Data instance to register
     * @return The registered data instance
     * @throws IllegalStateException If duplicate key is registered
     */
    protected <V extends Data<?>> V register(V value) {
        final String key = value.key();
        if (dataMap.containsKey(key)) {
            throw new IllegalStateException("Duplicate data field key: " + key);
        }
        dataMap.put(key, value);
        return value;
    }

    /**
     * Retrieves a data field's value by key with type validation.
     *
     * @param <T>  Expected data type
     * @param key  Key of the registered data field
     * @param type Class object representing expected type
     * @return Current value of the field
     * @throws IllegalArgumentException If no field exists for the key
     * @throws ClassCastException If value doesn't match requested type
     */
    public <T> T find(String key, Class<T> type) {
        Data<?> data = dataMap.get(key);
        if (data == null) {
            throw new IllegalArgumentException("No data field registered for key: " + key);
        }

        Object rawValue = data.get();
        if (type.isInstance(rawValue)) {
            return type.cast(rawValue);
        }

        throw new ClassCastException("Type mismatch for key '" + key + "'. " +
                "Expected: " + type.getSimpleName() + ", Actual: " +
                (rawValue != null ? rawValue.getClass().getSimpleName() : "null"));
    }

    /**
     * Safely retrieves a data field's value, returning null on errors.
     *
     * @param <T>  Expected data type
     * @param key  Key of the registered data field
     * @param type Class object representing expected type
     * @return Current value or null if key/type invalid
     */
    public <T> T findOrNull(String key, Class<T> type) {
        try {
            return find(key, type);
        } catch (IllegalArgumentException | ClassCastException e) {
            return null;
        }
    }

    /**
     * Gets the Data wrapper instance for a registered field.
     *
     * @param <V> Concrete Data type
     * @param key Key of the registered data field
     * @return Data instance or null if not found
     */
    @SuppressWarnings("unchecked")
    public <V extends Data<?>> V getValue(String key) {
        return (V) dataMap.get(key);
    }

    protected Data<Integer> intData(String key, int defaultValue) {
        return register(new Data<Integer>(key, defaultValue) {
            @Override
            public void deserialize(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
                set(nbt.getInt(key()));
            }

            @Override
            public void serialize(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
                nbt.putInt(key(), get());
            }
        });
    }

    protected Data<Float> floatData(String key, float defaultValue) {
        return register(new Data<Float>(key, defaultValue) {
            @Override
            public void deserialize(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
                set(nbt.getFloat(key()));
            }

            @Override
            public void serialize(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
                nbt.putFloat(key(), get());
            }
        });
    }

    protected Data<Double> doubleData(String key, double defaultValue) {
        return register(new Data<Double>(key, defaultValue) {
            @Override
            public void deserialize(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
                set(nbt.getDouble(key()));
            }

            @Override
            public void serialize(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
                nbt.putDouble(key(), get());
            }
        });
    }

    protected Data<Long> longData(String key, long defaultValue) {
        return register(new Data<Long>(key, defaultValue) {
            @Override
            public void deserialize(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
                set(nbt.getLong(key()));
            }

            @Override
            public void serialize(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
                nbt.putLong(key(), get());
            }
        });
    }

    protected Data<Short> shortData(String key, short defaultValue) {
        return register(new Data<Short>(key, defaultValue) {
            @Override
            public void deserialize(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
                set(nbt.getShort(key()));
            }

            @Override
            public void serialize(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
                nbt.putShort(key(), get());
            }
        });
    }

    protected Data<Byte> byteData(String key, byte defaultValue) {
        return register(new Data<Byte>(key, defaultValue) {
            @Override
            public void deserialize(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
                set(nbt.getByte(key()));
            }

            @Override
            public void serialize(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
                nbt.putByte(key(), get());
            }
        });
    }

    protected Data<Boolean> booleanData(String key, boolean defaultValue) {
        return register(new Data<Boolean>(key, defaultValue) {
            @Override
            public void deserialize(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
                set(nbt.getBoolean(key()));
            }

            @Override
            public void serialize(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
                nbt.putBoolean(key(), get());
            }
        });
    }

    protected Data<ItemStack> itemStackData(String key, ItemStack defaultValue) {
        return register(new Data<ItemStack>(key, defaultValue) {
            @Override
            public void deserialize(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
                if (nbt.contains(key())) {
                    ItemStack.fromNbt(registries, nbt.getCompound(key())).ifPresent(this::set);
                }
            }

            @Override
            public void serialize(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
                NbtCompound stackNbt = new NbtCompound();
                get().encode(registries, stackNbt);
                nbt.put(key(), stackNbt);
            }
        });
    }

    protected Data<UUID> uuidData(String key, UUID defaultValue) {
        return register(new Data<UUID>(key, defaultValue) {
            @Override
            public void deserialize(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
                set(nbt.getUuid(key()));
            }

            @Override
            public void serialize(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
                nbt.putUuid(key(), get());
            }
        });
    }
}
