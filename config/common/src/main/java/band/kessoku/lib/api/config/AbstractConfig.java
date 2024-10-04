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
package band.kessoku.lib.api.config;

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

import band.kessoku.lib.api.KessokuLib;
import band.kessoku.lib.api.config.annotations.Comment;
import band.kessoku.lib.api.config.annotations.Comments;
import band.kessoku.lib.api.config.annotations.Name;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.core.util.ReflectionUtil;

@SuppressWarnings({"rawtypes", "unused"})
public abstract class AbstractConfig {
    private final List<Consumer<AbstractConfig>> preSave = new ArrayList<>();
    private final List<Consumer<AbstractConfig>> preLoad = new ArrayList<>();
    private final List<BiConsumer<AbstractConfig, Boolean>> postSave = new ArrayList<>();
    private final List<BiConsumer<AbstractConfig, Boolean>> postLoad = new ArrayList<>();
    private List<Field> values;
    private List<Field> categories;
    private boolean split = false;

    public AbstractConfig() {
    }

    public boolean save() {
        preSave.forEach(consumer -> consumer.accept(this));
        File file = this.getPath().toFile();
        ConfigSerializer serializer = this.getSerializer();

        boolean result = true;
        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            writer.write(serializer.serialize(this.serialize()));
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

        boolean result = true;
        try {
            Map<String, Object> map = serializer.deserialize(FileUtils.readFileToString(file, StandardCharsets.UTF_8));
            // Put values into the config
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object cValue = entry.getValue();

                ConfigValue value;
                // Check the value is public and not static
                try {
                    Field field = this.getClass().getField(key);
                    if (!ModifiersUtil.isPublicOrStatic(field, true, false)) {
                        continue;
                    }
                    value = (ConfigValue) ReflectionUtil.getFieldValue(field, this);
                } catch (NoSuchFieldException e) {
                    continue;
                }

                ConfigValue.Type type = ConfigValue.Type.asType(cValue);
                // Check if the type is valid to deserialize
                if (type == ConfigValue.Type.NULL) {
                    KessokuLib.getLogger().error(KessokuConfig.MARKER, "Illegal type`{}` found in the file!", cValue.getClass().getName());
                    continue;
                }

                // Check if the type matches the value's type
                if (value.getType() != type) {
                    KessokuLib.getLogger().error(KessokuConfig.MARKER, "Illegal type`{}` found in the file! Expect {}.", type.toString().toLowerCase(), value.getType().toString().toLowerCase());
                    continue;
                }

                value.setTo(cValue);
            }
        } catch (IOException e) {
            result = false;
        }

        final boolean finalResult = result;
        postLoad.forEach(biConsumer -> biConsumer.accept(this, finalResult));
        return finalResult;
    }

    public void registerPreSaveListener(Consumer<AbstractConfig> preSave) {
        this.preSave.add(preSave);
    }

    public void registerPreLoadListener(Consumer<AbstractConfig> preLoad) {
        this.preLoad.add(preLoad);
    }

    public void registerPostSaveListener(BiConsumer<AbstractConfig, Boolean> postSave) {
        this.postSave.add(postSave);
    }

    public void registerPostLoadListener(BiConsumer<AbstractConfig, Boolean> postLoad) {
        this.postLoad.add(postLoad);
    }

    public void reset() {
        this.getValidValues().forEach(ConfigValue::reset);
        this.getValidCategories().forEach(AbstractConfig::reset);
    }

    public ImmutableList<ConfigValue<?, ?>> getValidValues() {
        if (this.values != null) {
            return ImmutableList.<ConfigValue<?, ?>>builder().addAll(this.values.parallelStream().map(field ->
                    (ConfigValue<?, ?>) ReflectionUtil.getFieldValue(field, this)).toList()).build();
        }

        List<Field> fields = new ArrayList<>();
        for (Field declaredField : this.getClass().getDeclaredFields()) {
            final boolean flag0 = ReflectUtil.isAssignableFrom(declaredField, ConfigValue.class);
            final boolean flag1 = ModifiersUtil.isPublicOrStatic(declaredField, true, false);
            if (flag0 && flag1) {
                fields.add(declaredField);
            }
        }

        this.values = fields;
        return this.getValidValues();
    }

    public ImmutableList<AbstractConfig> getValidCategories() {
        if (this.categories != null){
            return ImmutableList.<AbstractConfig>builder().addAll(this.categories.stream().map(field ->
                (AbstractConfig) ReflectionUtil.getFieldValue(field, this)).toList()).build();
        }

        List<Field> fields = new ArrayList<>();
        for (Field declaredField : this.getClass().getDeclaredFields()) {
            declaredField.setAccessible(true);

            final boolean flag0 = ReflectUtil.isAssignableFrom(declaredField, AbstractConfig.class);
            final boolean flag1 = ModifiersUtil.isPublic(declaredField);
            if (flag0 && flag1){
                fields.add(declaredField);
            }
        }

        this.categories = fields;
        return this.getValidCategories();
    }

    private ImmutableList<Field> getValidFields() {
        ImmutableList.Builder<Field> builder = ImmutableList.builder();
        for (Field declaredField : this.getClass().getDeclaredFields()) {
            final boolean flag0 = ReflectUtil.isAssignableFrom(declaredField, AbstractConfig.class, ConfigValue.class);
            final boolean flag1 = Modifier.isPublic(declaredField.getModifiers());

            if (flag0 && flag1){
                builder.add(declaredField);
            }
        }
        return builder.build();
    }

    private Map<String, ValueWithComment> serialize() {
        ImmutableMap.Builder<String, ValueWithComment> builder = ImmutableMap.builder();
        for (Field field : this.getValidFields()) {
            ReflectionUtil.makeAccessible(field);

            final String name = field.isAnnotationPresent(Name.class) ? field.getAnnotation(Name.class).value() : field.getName();
            final String[] comments = field.isAnnotationPresent(Comments.class) ? (String[]) Arrays.stream(field.getAnnotation(Comments.class).value()).map(Comment::value).toArray() : new String[0];

            Object fieldValue = ReflectionUtil.getFieldValue(field, this);

            // ConfigValue
            if (fieldValue instanceof ConfigValue<?, ?> value) {
                builder.put(name, new ValueWithComment(value.get(), comments));
                continue;
            }

            // Category
            AbstractConfig category = (AbstractConfig) fieldValue;
            if (this.split) {
                if (!category.save()) {
                    KessokuLib.getLogger().error(KessokuConfig.MARKER, "Failed to save category `{}!`", category.getSimpleName());
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

    /**
     * Get config full name. If it's a sub file, it will include the paths.
     * @return Get the full name of the config.
     */
    // Not including file ext, but parent path `/`
    public String getName() {
        Name name = this.getClass().getAnnotation(Name.class);
        return name == null ? this.getClass().getSimpleName() : name.value();
    }

    /**
     * Get the config name. Even it's a sub file, just config name.
     * @return The simple config name.
     */
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
