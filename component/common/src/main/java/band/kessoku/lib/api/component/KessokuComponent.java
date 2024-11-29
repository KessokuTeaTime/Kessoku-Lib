package band.kessoku.lib.api.component;

import band.kessoku.lib.api.registry.KessokuRegistry;
import band.kessoku.lib.impl.Progress;
import net.minecraft.component.ComponentType;
import net.minecraft.util.Identifier;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class KessokuComponent {
    public static final String MOD_ID = "kessoku_component";
    public static final String NAME = "Kessoku Component API";
    public static final Marker MARKER = MarkerFactory.getMarker("[" + NAME +"]");

    public static final ComponentType<Progress> PROGRESS_COMPONENT_TYPE;

    public static void init() {

    }

    static {
        PROGRESS_COMPONENT_TYPE = KessokuRegistry.registerComponentType(
                Identifier.of("kessoku", "progress"),
                builder -> builder.codec(Progress.CODEC).packetCodec(Progress.PACKET_CODEC)
        );
    }
}
