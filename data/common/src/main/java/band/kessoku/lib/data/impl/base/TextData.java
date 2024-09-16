package band.kessoku.lib.data.impl.base;

import band.kessoku.lib.data.api.NBTSerializable;
import band.kessoku.lib.data.impl.BaseData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;

import static net.minecraft.block.entity.BlockEntity.tryParseCustomName;

public final class TextData extends BaseData<Text> implements NBTSerializable {
    private TextData(String id, Text defaultValue) {
        super(id, defaultValue);
    }

    public static TextData create(String id, Text defaultValue) {
        return new TextData(id, defaultValue);
    }

    @Override
    public void write(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        if (get() != null)
            nbt.putString(id(), Text.Serialization.toJsonString(get(), registries));
    }

    @Override
    public void read(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        Text text = tryParseCustomName(nbt.getString(id()), registries);
        if (text != null)
            set(text);
    }
}
