package band.kessoku.lib.config.api.serializers.night;

import com.electronwill.nightconfig.core.*;
import com.electronwill.nightconfig.core.io.ConfigParser;
import com.electronwill.nightconfig.core.io.ConfigWriter;
import com.electronwill.nightconfig.json.FancyJsonWriter;
import com.electronwill.nightconfig.json.JsonParser;
import org.quiltmc.config.api.Config;
import org.quiltmc.config.api.MarshallingUtils;
import org.quiltmc.config.api.Serializer;
import org.quiltmc.config.api.values.ConfigSerializableObject;
import org.quiltmc.config.api.values.TrackedValue;
import org.quiltmc.config.api.values.ValueKey;
import org.quiltmc.config.api.values.ValueList;
import org.quiltmc.config.api.values.ValueMap;
import org.quiltmc.config.api.values.ValueTreeNode;
import org.quiltmc.config.impl.util.SerializerUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * A default serializer that writes in the <a href="https://www.json.org/">JSON format</a>.
 *
 * @implNote When passing entries to {@link com.electronwill.nightconfig.core.Config#add(String, Object)}, the string key is automatically split at each dot ({@code .}).
 * This completely breaks TOML serialization, since we allow dots in keys, using either {@link org.quiltmc.config.api.annotations.SerializedName} or {@link ValueMap}, whose keys are not validated for certain characters.
 * To get around this, use {@link com.electronwill.nightconfig.core.Config#add(List, Object)} via passing your key into {@link #toNightConfigSerializable(ValueKey)}.
 */
public final class JsonSerializer implements Serializer {
    public static final JsonSerializer INSTANCE = new JsonSerializer();
    private final ConfigParser<com.electronwill.nightconfig.core.Config> parser = new JsonParser();
    private final ConfigWriter writer = new FancyJsonWriter();

    private JsonSerializer() {

    }

    @Override
    public String getFileExtension() {
        return "json";
    }

    @Override
    public void serialize(Config config, OutputStream to) {
        this.writer.write(write(config, createUncommentedConfig(), config.nodes()), to);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void deserialize(Config config, InputStream from) {
        com.electronwill.nightconfig.core.Config read = this.parser.parse(from);

        for (TrackedValue<?> trackedValue : config.values()) {
            List<ValueKey> keyOptions = SerializerUtils.getPossibleKeys(config, trackedValue);

            for (ValueKey key : keyOptions) {
                String stringKey = key.toString();

                if (read.contains(stringKey)) {
                    ((TrackedValue) trackedValue).setValue(MarshallingUtils.coerce(read.get(stringKey), trackedValue.getDefaultValue(), (com.electronwill.nightconfig.core.Config c, MarshallingUtils.MapEntryConsumer entryConsumer) ->
                            c.entrySet().forEach(e -> entryConsumer.put(e.getKey(), e.getValue()))), false);
                }
            }
        }
    }

    private static List<Object> convertList(List<?> list) {
        List<Object> result = new ArrayList<>(list.size());

        for (Object value : list) {
            result.add(convertAny(value));
        }

        return result;
    }

    private static UnmodifiableConfig convertMap(ValueMap<?> map) {
        com.electronwill.nightconfig.core.Config result = createUncommentedConfig();

        for (Map.Entry<String, ?> entry : map.entrySet()) {
            List<String> key = new ArrayList<>();
            key.add(entry.getKey());
            result.add(key, convertAny(entry.getValue()));
        }

        return result;
    }

    private static Object convertAny(Object value) {
        if (value instanceof ValueMap) {
            return convertMap((ValueMap<?>) value);
        } else if (value instanceof ValueList) {
            return convertList((ValueList<?>) value);
        } else if (value instanceof ConfigSerializableObject) {
            return convertAny(((ConfigSerializableObject<?>) value).getRepresentation());
        } else {
            return value;
        }
    }

    private static com.electronwill.nightconfig.core.Config write(Config config, com.electronwill.nightconfig.core.Config uncommentedConfig, Iterable<ValueTreeNode> nodes) {
        for (ValueTreeNode node : nodes) {
            ValueKey key = SerializerUtils.getSerializedKey(config, node);

            if (node instanceof TrackedValue<?>) {
                TrackedValue<?> value = (TrackedValue<?>) node;

                uncommentedConfig.add(toNightConfigSerializable(key), convertAny(value.getRealValue()));
            } else {
                write(config, uncommentedConfig, ((ValueTreeNode.Section) node));
            }
        }

        return uncommentedConfig;
    }

    private static com.electronwill.nightconfig.core.Config createUncommentedConfig() {
        return InMemoryFormat.defaultInstance().createConfig(LinkedHashMap::new);
    }

    private static List<String> toNightConfigSerializable(ValueKey key) {
        List<String> listKey = new ArrayList<>();
        key.forEach(listKey::add);
        return listKey;
    }
}
