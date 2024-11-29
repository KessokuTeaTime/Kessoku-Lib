package band.kessoku.lib.impl;

import band.kessoku.lib.api.data.DataStructure;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public class Progress implements DataStructure {
    public static final Codec<Progress> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("time").forGetter(Progress::time),
            Codec.INT.fieldOf("totalTime").forGetter(Progress::totalTime)
    ).apply(instance, Progress::new));
    public static final PacketCodec<RegistryByteBuf, Progress> PACKET_CODEC = new PacketCodec<>() {
        @Override
        public Progress decode(RegistryByteBuf buf) {
            return new Progress(buf.readInt(), buf.readInt());
        }

        @Override
        public void encode(RegistryByteBuf buf, Progress value) {
            buf.writeInt(value.time());
            buf.writeInt(value.totalTime());
        }
    };

    private int time = 0;
    private int totalTime;

    private Progress(int time, int totalTime) {
        this.time = time;
        this.totalTime = totalTime;

    }

    public Progress(int totalTime) {
        this.totalTime = totalTime;
    }

    public void process() {
        process(1);
    }

    public void process(int time) {
        this.time += time;
    }

    public void reset(int totalTime) {
        this.time = 0;
        this.totalTime = totalTime;
    }

    public int time() {
        return this.time;
    }

    public int totalTime() {
        return this.totalTime;
    }

    public float decimal() {
        if (totalTime == 0)
            return 0.0f;
        return ((float) time) / totalTime;
    }
}
