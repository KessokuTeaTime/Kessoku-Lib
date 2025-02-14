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
package band.kessoku.lib.impl.networking.payload;

import band.kessoku.lib.api.networking.PacketByteBufHelper;

import net.minecraft.network.PacketByteBuf;

public class PayloadHelper {
    public static void write(PacketByteBuf byteBuf, PacketByteBuf data) {
        byteBuf.writeBytes(data.copy());
    }

    public static PacketByteBuf read(PacketByteBuf byteBuf, int maxSize) {
        assertSize(byteBuf, maxSize);

        PacketByteBuf newBuf = PacketByteBufHelper.create();
        newBuf.writeBytes(byteBuf.copy());
        byteBuf.skipBytes(byteBuf.readableBytes());
        return newBuf;
    }

    private static void assertSize(PacketByteBuf buf, int maxSize) {
        int size = buf.readableBytes();

        if (size < 0 || size > maxSize) {
            throw new IllegalArgumentException("Payload may not be larger than " + maxSize + " bytes");
        }
    }
}
