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
package band.kessoku.lib.impl.entrypoint.exceptions;

public final class LanguageAdapterException extends Exception {
    /**
     * Creates a new language adapter exception.
     *
     * @param s the message
     */
    public LanguageAdapterException(String s) {
        super(s);
    }

    /**
     * Creates a new language adapter exception.
     *
     * @param t the cause
     */
    public LanguageAdapterException(Throwable t) {
        super(t);
    }

    /**
     * Creates a new language adapter exception.
     *
     * @param s the message
     * @param t the cause
     */
    public LanguageAdapterException(String s, Throwable t) {
        super(s, t);
    }
}
