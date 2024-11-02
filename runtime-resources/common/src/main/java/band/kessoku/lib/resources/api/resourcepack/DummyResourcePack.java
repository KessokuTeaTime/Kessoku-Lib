package band.kessoku.lib.resources.api.resourcepack;

import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackInfo;
import net.minecraft.text.Text;

public interface DummyResourcePack extends ResourcePack {

    Text getDisplayName();

    Text getDescription();

    @Override
    ResourcePackInfo getInfo();

}
