package band.kessoku.lib.resources.api.provider;

import band.kessoku.lib.platform.api.Env;
import net.minecraft.util.Identifier;

public interface IProvider {

    Env[] getEnv();

    Identifier getId();

}
