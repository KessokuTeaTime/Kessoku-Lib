package band.kessoku.lib.resources;

import band.kessoku.lib.resources.api.IProvider;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class KessokuResourcesManager {

    private final String modid;

    private final List<IProvider> providers = new ArrayList<>();

    private KessokuResourcesManager(String modid) {
        this.modid = modid;
    }

    public static KessokuResourcesManager create(String modid) {
        return new KessokuResourcesManager(modid);
    }

    public void addProvider(IProvider provider) {
        providers.add(provider);
    }

    public String getModid() {
        return modid;
    }

    public List<IProvider> getProviders() {
        return ImmutableList.copyOf(providers);
    }
}
