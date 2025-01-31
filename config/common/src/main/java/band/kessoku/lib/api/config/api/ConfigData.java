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
package band.kessoku.lib.api.config.api;

import java.util.List;
import java.util.function.Function;

/**
 * Record the config's raw data, by encode or decode from config. <br>
 * Determine the {@link Name name}, {@link ConfigValue data wrapping}, {@link Comment comment},
 *
 * @param key The key of config field.
 * @param rawValue The raw data of config field's value.
 * @param comments The comment for config field.
 *
 * @author AmarokIce
 */
public record ConfigData(String key, String rawValue, List<String> comments) {
    public static final Function<ConfigData, String> JSON_FORMATTER = it ->
            String.format("\"%s\"", it.key()) + ":" + it.rawValue();

    public static final Function<ConfigData, String> JSON5_FORMATTER = it -> {
        StringBuilder builder = new StringBuilder();
        it.comments().forEach(comment -> builder.append("// ").append(comment).append("\n"));
        builder.append(String.format("\"%s\"", it.key()))
                .append(":")
                .append(it.rawValue());
        return builder.toString();
    };

    public static final Function<ConfigData, String> TOML_FORMATTER = it -> {
        StringBuilder builder = new StringBuilder();
        it.comments().forEach(commit -> builder
                .append("# ")
                .append(commit)
                .append("\n"));

        builder.append(it.key())
                .append("=")
                .append(it.rawValue());

        return builder.toString();
    };

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for(String comment: comments) {
            builder.append("//").append(comment).append("\n");
        }

        builder.append(key).append(": ").append(rawValue);

        return builder.toString();
    }

    /**
     * To string with formatter.
     * @param formatter the formatter. {@link Config.abcdefghijklmnopqrstuvwxyz({[}
     * @return
     */
    public String toString(Function<ConfigData, String> formatter) {
        return formatter.apply(this);
    }

    @Override
    public boolean equals(Object o) {
        return this.getClass() == o.getClass() && this.hashCode() == o.hashCode();
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}
