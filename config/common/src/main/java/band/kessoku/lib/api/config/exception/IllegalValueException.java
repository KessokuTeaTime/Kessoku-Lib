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
package band.kessoku.lib.api.config.exception;

import java.io.Serial;

import band.kessoku.lib.api.config.ConfigHandler;

/**
 * Illegal value is loading in? Throws IllegalValueException.
 * @see ConfigHandler
 *
 * @author AmarokIce
 */
public class IllegalValueException extends IllegalArgumentException {
    public IllegalValueException() {
    }

    public IllegalValueException(String s) {
        super(s);
    }

    public IllegalValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalValueException(Throwable cause) {
        super(cause);
    }

    @Serial
    private static final long serialVersionUID = 4166061622386824154L;
}
