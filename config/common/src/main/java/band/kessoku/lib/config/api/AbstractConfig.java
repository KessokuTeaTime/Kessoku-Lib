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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import band.kessoku.lib.base.ModUtils;
import band.kessoku.lib.config.KessokuConfig;
import band.kessoku.lib.config.api.annotations.Comment;
import band.kessoku.lib.config.api.annotations.Comments;
import band.kessoku.lib.config.api.annotations.Name;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

//@SuppressWarnings({"rawtypes", "unchecked", "unused"})
public abstract class AbstractConfig {
    private final List<Consumer> preSave = new ArrayList<>();
    private final List<Consumer> preLoad = new ArrayList<>();
    private final List<BiConsumer> postSave = new ArrayList<>();
    private final List<BiConsumer> postLoad = new ArrayList<>();
    private List<Field> values;
    private List<Field> categories;
    private boolean split = false;

    public AbstractConfig() {
    }

    public boolean save() {
        preSave.forEach(consumer -> consumer.accept(this));
        File file = this.getPath().toFile();
        ConfigSerializer serializer = this.getSerializer();
        boolean result;
        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            writer.write(serializer.serialize(this.serialize()));
            result = true;
        } catch (IOException e) {
            result = false;
        }
        final boolean finalResult = result;
        postSave.forEach(biConsumer -> biConsumer.accept(this, finalResult));
        return finalResult;
    }

    public boolean load() {
        preLoad.forEach(consumer -> consumer.accept(this));
        ConfigSerializer serializer = this.getSerializer();
        File file = this.getPath().toFile();
        boolean result;
        try {
            Map<String, Object> map = serializer.deserialize(FileUtils.readFileToString(file, StandardCharsets.UTF_8));
            // Put values into the config
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String string = entry.getKey();
                Object object = entry.getValue();
                ConfigValue value;
                // Check the value is public and not static
                try {
                    Field field = this.getClass().getField(string);
                    if (!Modifier.isPublic(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) continue;
                    value = (ConfigValue<?, ?>) field.get(this);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    continue;
                }
                ConfigValue.Type type;
                // Check if the type is valid to deserialize
                try {
                    type = ConfigValue.Type.asType(object);
                } catch (IllegalArgumentException e) {
                    ModUtils.getLogger().error(KessokuConfig.MARKER, "Illegal type`{}` found in the file!", object.getClass().getName());
                    continue;
                }
                // Check if the type matches the value's type
                if (value.getType() != type) {
                    ModUtils.getLogger().error(KessokuConfig.MARKER, "Illegal type`{}` found in the file! Expect {}.", type.toString().toLowerCase(), value.getType().toString().toLowerCase());
                    continue;
                }
                value.setTo(object);
            }
            result = true;
        } catch (IOException e) {
            result = false;
        }
        final boolean finalResult = result;
        postLoad.forEach(biConsumer -> biConsumer.accept(this, finalResult));
        return finalResult;
    }

    public <T extends AbstractConfig> void registerPreSaveListener(Consumer<T> preSave) {
        this.preSave.add(preSave);
    }

    public <T extends AbstractConfig> void registerPreLoadListener(Consumer<T> preLoad) {
        this.preLoad.add(preLoad);
    }

    public <T extends AbstractConfig> void registerPostSaveListener(BiConsumer<T, Boolean> postSave) {
        this.postSave.add(postSave);
    }

    public <T extends AbstractConfig> void registerPostLoadListener(BiConsumer<T, Boolean> postLoad) {
        this.postLoad.add(postLoad);
    }

    public void reset() {
        this.getValidValues().forEach(ConfigValue::reset);
        this.getValidCategories().forEach(AbstractConfig::reset);
    }

    public ImmutableList<ConfigValue<?, ?>> getValidValues() {
        if (this.values != null)
            return ImmutableList.<ConfigValue<?, ?>>builder().addAll(this.values.stream().map(field -> {
                try {
                    return (ConfigValue<?, ?>) field.get(this);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }).toList()).build();
        List<Field> fields = new ArrayList<>();
        for (Field declaredField : this.getClass().getDeclaredFields()) {
            if (declaredField.getDeclaringClass().isAssignableFrom(ConfigValue.class) && Modifier.isPublic(declaredField.getModifiers()) && !Modifier.isStatic(declaredField.getModifiers()))
                fields.add(declaredField);
        }
        this.values = fields;
        return this.getValidValues();
    }

    public ImmutableList<AbstractConfig> getValidCategories() {
        if (this.categories != null)
            return ImmutableList.<AbstractConfig>builder().addAll(this.categories.stream().map(field -> {
                try {
                    return (AbstractConfig) field.get(this);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }).toList()).build();
        List<Field> fields = new ArrayList<>();
        for (Field declaredField : this.getClass().getDeclaredFields()) {
            if (declaredField.getDeclaringClass().isAssignableFrom(AbstractConfig.class) && Modifier.isPublic(declaredField.getModifiers()))
                fields.add(declaredField);
        }
        this.categories = fields;
        return this.getValidCategories();
    }

    private ImmutableList<Field> getValidFields() {
        ImmutableList.Builder<Field> builder = ImmutableList.builder();
        for (Field declaredField : this.getClass().getDeclaredFields()) {
            if ((declaredField.getDeclaringClass().isAssignableFrom(AbstractConfig.class) || declaredField.getDeclaringClass().isAssignableFrom(ConfigValue.class)) && Modifier.isPublic(declaredField.getModifiers()))
                builder.add(declaredField);
        }
        return builder.build();
    }

    private Map<String, ValueWithComment> serialize() {
        ImmutableMap.Builder<String, ValueWithComment> builder = ImmutableMap.builder();
        for (Field field : this.getValidFields()) {
            final String name = field.isAnnotationPresent(Name.class) ? field.getAnnotation(Name.class).value() : field.getName();
            final String[] comments = field.isAnnotationPresent(Comments.class) ? (String[]) Arrays.stream(field.getAnnotation(Comments.class).value()).map(Comment::value).toArray() : new String[0];
            Object o;
            try {
                o = field.get(this);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            // ConfigValue
            if (o instanceof ConfigValue<?, ?> value) {
                builder.put(name, new ValueWithComment(value.get(), comments));
                continue;
            }
            // Category
            AbstractConfig category = (AbstractConfig) o;
            if (this.split) {
                if (!category.save()) {
                    ModUtils.getLogger().error(KessokuConfig.MARKER, "Failed to save category `{}!`", category.getSimpleName());
                }
                continue;
            }
            builder.put(name, new ValueWithComment(category.serialize(), comments));
        }
        return builder.build();
    }

    protected void splitToFiles() {
        this.split = true;
    }

    public boolean isSplitToFiles() {
        return this.split;
    }

    // Not including file ext, but parent path `/`
    public String getName() {
        Name name = this.getClass().getAnnotation(Name.class);
        return name == null ? this.getClass().getSimpleName() : name.value();
    }

    // Just config name
    public String getSimpleName() {
        String[] strings = this.getName().split("/");
        return strings[strings.length - 1];
    }

    public Path getPath() {
        return Path.of(FilenameUtils.normalize(this.getName() + "." + this.getSerializer().getFileExtension()).replace('/', File.separatorChar));
    }

    public ConfigSerializer getSerializer() {
        return KessokuConfig.getSerializerInstance(Objects.requireNonNull(KessokuConfig.getSerializer(this), String.format("Config %s isn't registered!", this.getName())));
    }

    public record ValueWithComment(Object object, String[] comments) {
    }
}
