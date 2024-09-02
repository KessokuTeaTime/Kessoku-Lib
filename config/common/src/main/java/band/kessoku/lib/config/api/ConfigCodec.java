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
package band.kessoku.lib.config.api;

import java.util.function.Function;

public abstract class ConfigCodec<A, B> {
    private final Function<A, B> encode;
    private final Function<B, A> decode;

    public ConfigCodec(Function<A, B> encode, Function<B, A> decode) {
        this.encode = encode;
        this.decode = decode;
    }

    public B encode(A value) {
        return encode.apply(value);
    }

    public A decode(B value) {
        return decode.apply(value);
    }
}
