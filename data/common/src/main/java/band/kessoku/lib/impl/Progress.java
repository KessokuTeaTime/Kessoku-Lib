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
package band.kessoku.lib.impl;

import band.kessoku.lib.api.data.Timer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public class Progress implements Timer {
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

    public static Progress create(int totalTime) {
        return new Progress(0, totalTime);
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
