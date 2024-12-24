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
package band.kessoku.lib.impl.config;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import band.kessoku.lib.api.KessokuLib;
import band.kessoku.lib.api.base.reflect.ModifiersUtil;
import band.kessoku.lib.api.base.reflect.ReflectUtil;
import band.kessoku.lib.api.config.ConfigSerializer;
import band.kessoku.lib.api.config.ConfigValue;
import band.kessoku.lib.api.config.KessokuConfig;
import band.kessoku.lib.api.config.annotations.Name;
import band.kessoku.lib.api.config.values.MapValue;
import band.kessoku.lib.api.config.values.StringValue;
import org.apache.logging.log4j.core.util.ReflectionUtil;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;

@SuppressWarnings({"unused", "UnusedReturnValue", "unchecked", "rawtypes"})
// todo Change the name
public class AbstractConfig implements ConfigValue<AbstractConfig, MapValue<StringValue, ? extends ConfigValue<?, ?>>>, Cloneable {
    private List<Field> valuesCache;
    private List<Field> categoriesCache;

    public AbstractConfig() {
    }

    @Override
    public final void setFrom(final AbstractConfig value) {
        this.setTo(value.getTo());
    }

    @Override
    public final void setTo(final MapValue<StringValue, ? extends ConfigValue<?, ?>> value) {
        for (final Map.Entry<StringValue, ? extends ConfigValue<?, ?>> entry : value.entrySet()) {
            final String key = entry.getKey().getTo();
            final ConfigValue<?, ?> fromConfigValue = entry.getValue();
            final Field field;
            try {
                field = this.getClass().getField(key);
            } catch (NoSuchFieldException e) {
                KessokuLib.getLogger().info(KessokuConfig.MARKER, "{} doesn't exist in {}!", key, this.getName());
                continue;
            }
            if (!ReflectUtil.isAssignableFrom(field, ConfigValue.class)) {
                KessokuLib.getLogger().info(KessokuConfig.MARKER, "{} from {} is not a ConfigValue!", key, this.getName());
                continue;
            }
            if (ModifiersUtil.isStatic(field)) {
                KessokuLib.getLogger().warn(KessokuConfig.MARKER, "Invalid ConfigValue {} found in config {}! It shouldn't be static but it is!", key, this.getName());
                continue;
            }
            ReflectionUtil.makeAccessible(field);
            final ConfigValue configValue;
            try {
                configValue = (ConfigValue) field.get(this);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (!configValue.getType().canCastFrom(fromConfigValue.getType())) {
                KessokuLib.getLogger().warn(KessokuConfig.MARKER, "{} and {} are incompatible!", fromConfigValue.getType().name(), configValue.getType().name());
            }
            configValue.setTo(fromConfigValue.getTo());
        }
    }

    @Override
    public final AbstractConfig getFrom() {
        return this;
    }

    @Override
    public final MapValue<StringValue, ? extends ConfigValue<?, ?>> getTo() {
        final MapValue mapValue = MapValue.of();
        this.getValidFields().forEach(field -> {
            final Name nameAnnotation = field.getAnnotation(Name.class);
            final String name = nameAnnotation == null ? field.getName() : nameAnnotation.value();
            try {
                mapValue.put(StringValue.of(name),(ConfigValue<?, ?>) field.get(this));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        return mapValue;
    }

    public final void reset() {
        this.getDeclaredValues().forEach(ConfigValue::reset);
        this.getDeclaredCategories().forEach(AbstractConfig::reset);
    }

    @Override
    public final Type getType() {
        return Type.MAP;
    }

    @Override
    public final AbstractConfig getDefaultFrom() {
        final AbstractConfig copy = (AbstractConfig) this.clone();
        copy.reset();
        return copy;
    }

    @Override
    public final MapValue<StringValue, ? extends ConfigValue<?, ?>> getDefaultTo() {
        return getDefaultFrom().getTo();
    }

    @Unmodifiable
    public final List<? extends ConfigValue<?, ?>> getDeclaredValues() {
        if (this.valuesCache == null) {
            this.valuesCache = this.getValidFields().stream().filter(field -> field.getDeclaringClass().isAssignableFrom(ConfigValue.class)).toList();
        }
        return this.valuesCache.stream().map(field ->
                (ConfigValue<?, ?>) ReflectionUtil.getFieldValue(field, this)).toList();
    }

    @Unmodifiable
    public final List<? extends AbstractConfig> getDeclaredCategories() {
        if (this.categoriesCache == null) {
            this.categoriesCache = this.getValidFields().stream().filter(field -> field.getDeclaringClass().isAssignableFrom(AbstractConfig.class)).toList();
        }
        return this.categoriesCache.stream().map(field ->
                (AbstractConfig) ReflectionUtil.getFieldValue(field, this)).toList();
    }

    private List<Field> getValidFields() {
        final List<Field> fields = new ArrayList<>();
        for (final Field declaredField : this.getClass().getDeclaredFields()) {
            final boolean isValidType = ReflectUtil.isAssignableFrom(declaredField, AbstractConfig.class, ConfigValue.class);
            final boolean isValidModifier = ModifiersUtil.isPublicOrStatic(declaredField, true, false);

            if (isValidType && isValidModifier) {
                fields.add(declaredField);
            }
        }
        return Collections.unmodifiableList(fields);
    }

    public final String getName() {
        final Name name = this.getClass().getAnnotation(Name.class);
        return name == null ? this.getClass().getSimpleName() : name.value();
    }

    /**
     * Get the path of this config
     */
    public final Path getPath() {
        // todo
        return Path.of("");
    }

    public final ConfigSerializer getSerializer() {
        return Objects.requireNonNull(KessokuConfig.getSerializer(this), String.format("Config %s isn't registered!", this.getName()));
    }

    public record WrappedValue(ConfigValue configValue, String[] comments) {
    }

    @Override
    @ApiStatus.Internal
    protected final Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            // Shouldn't happen
            throw new RuntimeException(e);
        }
    }
}
